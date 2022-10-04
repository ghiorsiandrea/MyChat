package org.ghiorsi.server;

import org.ghiorsi.commons.ShippingPackage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Server {

    public static HashMap<String, String> ALL_IPS = new HashMap<String, String>();

    public HashMap<String, String> getAllIps() {
        return ALL_IPS;
    }

    public void setAllIps(HashMap<String, String> allIps) {
        this.ALL_IPS = allIps;
    }

    public static void main(String[] args) {
        MarcoServidor miMarco = new MarcoServidor();
        miMarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static final int PORT = Integer.parseInt(System.getenv("PORT"));

    public static final String ONLINE = " Online";

    public static class MarcoServidor extends JFrame implements Runnable {
        JTextArea areatexto;

        public MarcoServidor() {
            setBounds(1200, 300, 280, 350);
            JPanel milamina = new JPanel();
            milamina.setLayout(new BorderLayout());
            areatexto = new JTextArea();
            milamina.add(areatexto, BorderLayout.CENTER);
            add(milamina);
            setVisible(true);

            // In order to the Server through the socket listens to the Client at all times, we will use a Tread
            Thread mihilo = new Thread(this);
            mihilo.start();
        }

        @Override
        public void run() {
            System.out.println("Welcome to My Chat");
            try {
                ServerSocket servidor = new ServerSocket();
                servidor.bind(new InetSocketAddress("0.0.0.0", PORT));

                String nick, mensaje;
                ShippingPackage paquete_recibido;


                while (true) {
                    Socket misocket = servidor.accept();

                    ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());
                    paquete_recibido = (ShippingPackage) paquete_datos.readObject();
                    nick = paquete_recibido.getNick();
                    mensaje = paquete_recibido.getMensaje();
                    String localizacionIp = misocket.getInetAddress().getHostAddress();

                    if (!mensaje.equals(ONLINE)) {
                        areatexto.append("\n" + "FROM: " + nick + ", TO: " + localizacionIp + " " + "\n" + "" + mensaje + "");

                        // Communication bridge through which the data will flow to be forwarded

                        Socket enviaDestinatario = new Socket(localizacionIp, 9090);
                        ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                        paqueteReenvio.writeObject(paquete_recibido);
                        enviaDestinatario.close();
                        misocket.close();

                    } else {

                        //     -------  Detecta Online    -------
                        ALL_IPS.put(nick, localizacionIp);

                        Set<String> allNicks = ALL_IPS.keySet();
                        ArrayList<String> listOfNicks = new ArrayList<String>(allNicks);
                        Collection<String> allIps = ALL_IPS.values();

                        for (String ip : allIps) {

                            // Communication bridge through which the data will flow to be forwarded

                            Socket IpsSender = new Socket(ip, 9090);
                            ShippingPackage nicksPackage = new ShippingPackage();
                            nicksPackage.setNicks(listOfNicks);
                            nicksPackage.setMensaje(ONLINE);
                            ObjectOutputStream paqueteReenvio = new ObjectOutputStream(IpsSender.getOutputStream());
                            paqueteReenvio.writeObject(nicksPackage);
                            IpsSender.close();
                            misocket.close();
                        }

                        for (String z : allNicks) {
                            System.out.println("NICKS: " + z);
                        }
                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
