package org.ghiorsi.client;

import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.net.Socket;

public class Closer extends WindowAdapter {
    private final Socket socket;
    public Closer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}