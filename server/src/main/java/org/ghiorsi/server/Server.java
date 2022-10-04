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

public class Server {
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

                //ServerSocket servidor = new ServerSocket(PORT);

                String nick, ip, mensaje;
                ArrayList<String> listaIp = new ArrayList<String>();
                ShippingPackage paquete_recibido;
                while (true) {
                    Socket misocket = servidor.accept();

                    ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());
                    paquete_recibido = (ShippingPackage) paquete_datos.readObject();
                    nick = paquete_recibido.getNick();
                    ip = paquete_recibido.getIp();
                    mensaje = paquete_recibido.getMensaje();

                    if (!mensaje.equals(ONLINE)) {
                        areatexto.append("\n" + "De: " + nick + ", para: " + ip + " " + "\n" + "" + mensaje + "");

                        // Puente de comunicacion por donde fluiran los datos para reenviarse
                        Socket enviaDestinatario = new Socket(ip, 9090);
                        ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                        paqueteReenvio.writeObject(paquete_recibido);
                        enviaDestinatario.close();
                        misocket.close();
                    } else {
                        //     -------    Ex.Tree - Detecta Online    -------
                        InetAddress localizacion = misocket.getInetAddress();
                        String IpRemota = localizacion.getHostAddress();
                        //System.out.println("\nOnline con la IP " + IpRemota);

                        listaIp.add(IpRemota);

                        paquete_recibido.setIps(listaIp);

                        for(String z: listaIp) {
                            System.out.println("Array: " + z);

                            // Puente de comunicacion por donde fluiran los datos para reenviarse
                            Socket enviaDestinatario = new Socket(z, 9090);
                            ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                            paqueteReenvio.writeObject(paquete_recibido);
                            enviaDestinatario.close();
                            misocket.close();
                        }
                    }

                    //     -------    Ex.Tree - Detecta Online    -------

//                    Ex. One
//                    DataInputStream flujo_entrada = new DataInputStream(misocket.getInputStream());
//                    String mensaje_texto = flujo_entrada.readUTF();
//                    areatexto.append("\n" + mensaje_texto);
//                    misocket.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
