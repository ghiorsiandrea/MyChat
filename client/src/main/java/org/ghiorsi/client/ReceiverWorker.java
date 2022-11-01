package org.ghiorsi.client;

import org.ghiorsi.commons.ShippingPackage;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ReceiverWorker extends Thread {

    private final Map<String, Consumer<ShippingPackage>> receiverConsumerMap;
    private Socket receiverSocket;
    private LaminaMarcoCliente laminaMarcoCliente;

    public ReceiverWorker(Socket receiverSocket, LaminaMarcoCliente laminaMarcoCliente) {
        this.receiverSocket = receiverSocket;
        this.laminaMarcoCliente = laminaMarcoCliente;
        this.receiverConsumerMap = Map.of(
                Client.ONLINE,
                shippingPackage -> laminaMarcoCliente.updateNicks(shippingPackage.getNicks()),
                Client.ECHO_TEST,
                shippingPackage -> System.out.println("Echo Test recibido")
        );
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
                    Optional.ofNullable(receiverConsumerMap.get(paqueteRecibido.getMensaje()))
                            .ifPresentOrElse(consumer -> consumer.accept(paqueteRecibido),
                                    () -> laminaMarcoCliente.writeMessage(paqueteRecibido.getNickFrom(), paqueteRecibido.getMensaje()));
                    this.wait(1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
