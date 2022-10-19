package org.ghiorsi.client;

import org.ghiorsi.commons.ShippingPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Sender {

    private Socket senderSocket;

    public Sender(Socket senderSocket) {
      this.senderSocket = senderSocket;
    }
    public void sendMessage(String nickTo, String nickFrom, String message) throws IOException {
        ShippingPackage datos = new ShippingPackage();
        datos.setNickTo(nickTo);
        datos.setNickFrom(nickFrom);
        datos.setMensaje(message);
        ObjectOutputStream outputStream = new ObjectOutputStream(senderSocket.getOutputStream());
        outputStream.writeObject(datos);
    }
    public void notifyMyNick(String myNick) throws IOException {
        ShippingPackage datos = new ShippingPackage();
        datos.setNickTo(myNick);
        datos.setMensaje(Client.ONLINE);
        ObjectOutputStream outputStream = new ObjectOutputStream(senderSocket.getOutputStream());
        outputStream.writeObject(datos);
    }

}
