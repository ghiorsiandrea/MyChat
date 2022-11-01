package org.ghiorsi.server;

import org.ghiorsi.commons.ShippingPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class Notifiyer {

    public Notifiyer() {
    }

    public static void notifyAllNicks() {
        //TODO
        Collection<Socket> socketCollection = Server.MarcoServidor.NICKS_AND_CLIENT_DATA
                .values()
                .stream()
                .map(ClientData::getSocket)
                .toList();
        Set<String> nicksSet = Server.MarcoServidor.NICKS_AND_CLIENT_DATA.keySet();
        ShippingPackage nicksPackage = new ShippingPackage();
        nicksPackage.setNicks(new ArrayList<>(nicksSet));

        for (Socket socketsColl : socketCollection) {
            // Communication bridge through which the data will flow to be forwarded
            ObjectOutputStream paqueteReenvio = null;
            try {
                paqueteReenvio = new ObjectOutputStream(socketsColl.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                paqueteReenvio.writeObject(nicksPackage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Socket z : socketCollection) {
            System.out.println("SOCKETS ONLINE: " + z);
        }
    }
}
