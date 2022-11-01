package org.ghiorsi.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

public class ClientEventChecker extends Thread {

    private MessageManager messageManager;

    public ClientEventChecker() {
        this.messageManager = new MessageManager();
        this.setName("client-event-checker");
    }

    @Override
    public void run() {
        while (true) {
            List<ClientData> ClientDataList = new ArrayList<>(Server.MarcoServidor.NICKS_AND_CLIENT_DATA.values());
            for (ClientData clientData : ClientDataList) {
                try {
                    int available = clientData.getSocket().getInputStream().available();
                    boolean isDataForRead = available != 0;
                    Future<?> messageFuture = clientData.getMessageFuture();
                    boolean isLastMessageAlreadyDone = Objects.isNull(messageFuture) || messageFuture.isDone();
                    if (isDataForRead && isLastMessageAlreadyDone) {
                        Future<?> newMessageFuture = messageManager.processMessage(clientData.getSocket());
                        clientData.setMessageFuture(newMessageFuture);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }
}
