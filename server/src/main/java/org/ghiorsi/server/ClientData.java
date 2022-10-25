package org.ghiorsi.server;

import java.net.Socket;
import java.util.concurrent.Future;

public class ClientData {

    private final Socket socket;
    private Future<?> messageFuture;

    private final String nick;

    public ClientData(Socket socket, String nick) {
        this.socket = socket;
        this.nick = nick;
    }

    public Socket getSocket() {
        return socket;
    }

    public Future<?> getMessageFuture() {
        return messageFuture;
    }

    public void setMessageFuture(Future<?> messageFuture) {
        this.messageFuture = messageFuture;
    }

    public String getNick() {
        return nick;
    }
}
