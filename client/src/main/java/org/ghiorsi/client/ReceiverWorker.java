package org.ghiorsi.client;

import org.ghiorsi.commons.ShippingPackage;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ReceiverWorker extends Thread {

    private Socket receiverSocket;
    private LaminaMarcoCliente laminaMarcoCliente;

    public ReceiverWorker(Socket receiverSocket, LaminaMarcoCliente laminaMarcoCliente) {
        this.receiverSocket = receiverSocket;
        this.laminaMarcoCliente = laminaMarcoCliente;
    }

    @Override
    public void start() {
        try {
            int available = 1;
            while (true) {
                available = receiverSocket.getInputStream().available();
                if (available != 0) {
                    ObjectInputStream flujoentrada = new ObjectInputStream(receiverSocket.getInputStream());
                    ShippingPackage paqueteRecibido = (ShippingPackage) flujoentrada.readObject();

                    if (!paqueteRecibido.getMensaje().equals(Client.ONLINE)) {
                        laminaMarcoCliente.writeMessage(paqueteRecibido.getNickFrom(), paqueteRecibido.getMensaje());
                    } else {
                        laminaMarcoCliente.updateNicks(paqueteRecibido.getNicks());
                    }
                } else {
                    synchronized (this) {
                        try {
                            this.wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
