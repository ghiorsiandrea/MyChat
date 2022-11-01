package org.ghiorsi.server;

import org.ghiorsi.commons.ShippingPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
                    Notifiyer.notifyAllNicks();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
