package org.ghiorsi.server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static HashMap<String, String> ALL_NICKS_AND_IPS = new HashMap<String, String>();

    public HashMap<String, String> getAllIps() {
        return ALL_NICKS_AND_IPS;
    }

    public void setAllIps(HashMap<String, String> allNicksAndIps) {
        this.ALL_NICKS_AND_IPS = allNicksAndIps;
    }


    public static void main(String[] args) {
        MarcoServidor miMarco = new MarcoServidor(new NewConnectionManager(), new MessageManager());
        miMarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static final int PORT = Integer.parseInt(System.getenv("PORT"));
    public static final String ONLINE = " Online";

    public static class MarcoServidor extends JFrame implements Runnable {
        static Map<String, Socket> NICKS_AND_SOCKETS = Collections.synchronizedMap(new HashMap<>());
        static JTextArea areatexto;

        private NewConnectionManager newConnectionManager;

        private MessageManager messageManager;

        public MarcoServidor(NewConnectionManager newConnectionManager, MessageManager messageManager) {
            this.newConnectionManager = newConnectionManager;

            setBounds(1200, 300, 280, 350);
            JPanel milamina = new JPanel();
            milamina.setLayout(new BorderLayout());
            areatexto = new JTextArea();
            milamina.add(areatexto, BorderLayout.CENTER);
            add(milamina);
            areatexto.setBackground(Color.cyan);
            setVisible(true);

            // In order to the Server through the socket listens to the Client at all times, we will use a Tread
            Thread mihilo = new Thread(this);
            mihilo.start();
        }

        @Override
        public void run() {
            System.out.println("Welcome to My Chat");
            try (ServerSocket servidor = new ServerSocket()) {
                servidor.bind(new InetSocketAddress("0.0.0.0", PORT));

                while (true) {
                    Socket misocket = servidor.accept();
                    newConnectionManager.processConnection(misocket);
                    messageManager.processMessage(misocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}