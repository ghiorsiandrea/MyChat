package org.ghiorsi.server;

import org.ghiorsi.commons.ShippingPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import static org.ghiorsi.server.Server.ECHO_TEST;
import static org.ghiorsi.server.Server.ONLINE;

public class MessageManager {

    private ExecutorService pool;

    public MessageManager() {
        pool = Executors.newFixedThreadPool(6, new ThreadFactory() {
            private int count = 1;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("message-manager-" + count++);
                return thread;
            }
        });

    }


    public Future<?> processMessage(Socket newSocket) {
        return pool.submit(() -> {
            synchronized (newSocket) {
                String nickTo, nickFrom, mensaje;
                ShippingPackage paquete_recibido;
                ObjectInputStream paquete_datos;
                try {
                    paquete_datos = new ObjectInputStream(newSocket.getInputStream());
                    paquete_recibido = (ShippingPackage) paquete_datos.readObject();
                    nickTo = paquete_recibido.getNickTo();
                    nickFrom = paquete_recibido.getNickFrom();
                    mensaje = paquete_recibido.getMensaje();
                    //String localizacionIp = newSocket.getInetAddress().getHostAddress();
                    Socket otherSocket = Server.MarcoServidor.NICKS_AND_CLIENT_DATA.get(nickTo).getSocket();
                    if (!mensaje.equals(ONLINE) && !mensaje.equals(ECHO_TEST)) {
                        Server.MarcoServidor.areatexto.append("\n" + "FROM: " + nickFrom + ", TO: " + nickTo + " " +
                                "\n" + "" + mensaje + "");
                        // Communication bridge through which the data will flow to be forwarded
                        ObjectOutputStream paqueteReenvio;
                        paqueteReenvio = new ObjectOutputStream(otherSocket.getOutputStream());
                        paqueteReenvio.writeObject(paquete_recibido);
                    } else {
                        System.out.println("The client sent an unexpected message.");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
