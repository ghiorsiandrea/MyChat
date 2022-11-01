package org.ghiorsi.server;

import org.ghiorsi.commons.ShippingPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ConnectionChecker extends Thread {

    public ConnectionChecker() {
        this.setName("connection-checker");
    }

    @Override
    public void run() {
        while (true) {
            List<ClientData> ClientDataList = new ArrayList<>(Server.MarcoServidor.NICKS_AND_CLIENT_DATA.values());
            for (ClientData clientData : ClientDataList) {
                try {
                    ShippingPackage testPackage = new ShippingPackage();
                    testPackage.setMensaje(Server.ECHO_TEST);
                    ObjectOutputStream objectInputStream = new ObjectOutputStream(clientData.getSocket().getOutputStream());
                    objectInputStream.writeObject(testPackage);
                } catch (IOException ex) {
                    try {
                        clientData.getSocket().close();
                        System.out.println("Client disconnected. Socket closing...");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Server.MarcoServidor.NICKS_AND_CLIENT_DATA.remove(clientData.getNick());
                    Notifiyer.notifyAllNicks();
                } finally {
                    try {
                        this.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
