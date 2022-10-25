package org.ghiorsi.server;

import org.ghiorsi.commons.ShippingPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static org.ghiorsi.server.Server.ONLINE;

public class NewConnectionManager {

    private ExecutorService pool;

    public NewConnectionManager() {
        pool = Executors.newFixedThreadPool(6, new ThreadFactory() {
            private int count = 1;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("connection-manager-" + count++);
                return thread;
            }
        });

    }

    public void processConnection(Socket newSocket) {
        pool.submit(() -> {
            synchronized (newSocket) {
                String nick;
                ShippingPackage paquete_recibido;
                try {
                    ObjectInputStream paquete_datos = new ObjectInputStream(newSocket.getInputStream());
                    paquete_recibido = (ShippingPackage) paquete_datos.readObject();
                    nick = paquete_recibido.getNickTo();
                    Server.MarcoServidor.NICKS_AND_CLIENT_DATA.put(nick, new ClientData(newSocket, nick));
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
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
