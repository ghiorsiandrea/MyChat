package org.ghiorsi.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class IncomingMessageChecker extends Thread {

    private MessageManager messageManager;

    public IncomingMessageChecker() {
        this.messageManager = new MessageManager();
    }

    @Override
    public void start() {
        while (true) {
            List<Socket> socketList = new ArrayList<>(Server.MarcoServidor.NICKS_AND_SOCKETS.values());
            for (Socket socket : socketList) {
                try {
                    int available = socket.getInputStream().available();
                    boolean isDataForRead = available != 0;
                    if (isDataForRead) {
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
