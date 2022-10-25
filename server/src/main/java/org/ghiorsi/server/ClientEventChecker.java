package org.ghiorsi.server;

import org.ghiorsi.commons.ShippingPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Future;

import static org.ghiorsi.server.Server.ONLINE;

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
                    if (!clientData.getSocket().isConnected()) {
                        Server.MarcoServidor.NICKS_AND_CLIENT_DATA.remove(clientData.getNick());
                        Collection<Socket> socketCollection = Server.MarcoServidor.NICKS_AND_CLIENT_DATA
                                .values()
                                .stream()
                                .map(ClientData::getSocket)
                                .toList();
                        Set<String> nicksSet = Server.MarcoServidor.NICKS_AND_CLIENT_DATA.keySet();
                        ShippingPackage nicksPackage = new ShippingPackage();
                        nicksPackage.setNicks(new ArrayList<>(nicksSet));
                        nicksPackage.setMensaje(ONLINE);

                        for (Socket socketsColl : socketCollection) {

                            // Communication bridge through which the data will flow to be forwarded
                            ObjectOutputStream paqueteReenvio = new ObjectOutputStream(socketsColl.getOutputStream());
                            paqueteReenvio.writeObject(nicksPackage);
                        }
                        for (Socket z : socketCollection) {
                            System.out.println("SOCKETS ONLINE: " + z);
                        }
                        continue;
                    }
                    int available = clientData.getSocket().getInputStream().available();
                    boolean isDataForRead = available != 0;
                    Future<?> messageFuture = clientData.getMessageFuture();
                    boolean isLastMessageAlreadyDone = Objects.isNull(messageFuture) ? true : messageFuture.isDone();
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
