package org.ghiorsi.server;

import org.ghiorsi.commons.ShippingPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.ghiorsi.server.Server.ONLINE;

public class MessageManager {

    private ExecutorService pool;

    public MessageManager() {
        pool = Executors.newFixedThreadPool(6);
    }

    public void processMessage(Socket newSocket) {
        pool.submit(() -> {
            String nick, mensaje;
            ShippingPackage paquete_recibido = null;

            ObjectInputStream paquete_datos;
            try {
                paquete_datos = new ObjectInputStream(newSocket.getInputStream());
                paquete_recibido = (ShippingPackage) paquete_datos.readObject();
            } catch (IOException |
                     ClassNotFoundException e) {
                e.printStackTrace();
            }

            nick = paquete_recibido.getNick();
            mensaje = paquete_recibido.getMensaje();
            String localizacionIp = newSocket.getInetAddress().getHostAddress();

            if (!mensaje.equals(ONLINE)) {
                Server.MarcoServidor.areatexto.append("\n" + "FROM: " + nick + ", TO: " + localizacionIp + " " + "\n" + "" + mensaje + "");

                // Communication bridge through which the data will flow to be forwarded
                Socket enviaDestinatario = newSocket;
                ObjectOutputStream paqueteReenvio;
                try {
                    paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                    paqueteReenvio.writeObject(paquete_recibido);
                    enviaDestinatario.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
