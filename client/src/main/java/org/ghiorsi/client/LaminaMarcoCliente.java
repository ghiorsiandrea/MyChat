package org.ghiorsi.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

class LaminaMarcoCliente extends JPanel {
    private final JTextField campo1;
    private final JComboBox<String> nicks;
    private final JLabel nick;
    private final JTextArea campochat;
    private final JButton sendButton;

    public Sender mySender;

    public LaminaMarcoCliente(String userNick, Sender mySender) {

        this.mySender = mySender;

        nick = new JLabel();
        nick.setText(userNick);
        nick.setBackground(java.awt.Color.CYAN);
        add(nick);

        JLabel texto = new JLabel(" - Online - ");
        texto.setBackground(java.awt.Color.cyan);
        add(texto);

        nicks = new JComboBox<>();
        add(nicks);
        nicks.setBackground(java.awt.Color.cyan);

        campochat = new JTextArea(12, 20);
        add(campochat);
        campochat.setBackground(java.awt.Color.cyan);

        campo1 = new JTextField(20);
        add(campo1);
        campo1.setBackground(java.awt.Color.cyan);

        sendButton = new JButton("Send");
        TextSender mievento = new TextSender();
        sendButton.addActionListener(mievento);
        add(sendButton);
        sendButton.setBackground(java.awt.Color.CYAN);
    }

    public void updateNicks(List<String> allNicks) {
        nicks.removeAllItems();
        for (String z : allNicks) {
            nicks.addItem(z);
        }
    }

    public void writeMessage(String nick, String message) {
        campochat.append("\n" + nick + ": " + message);
    }

    private class TextSender implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String nickText = nick.getText();
            String campo1Text = campo1.getText();

            System.out.println("\n" + nickText + ": " + campo1Text);
            campochat.append("\n" + campo1Text);
            try {
                mySender.sendMessage(nickText, campo1Text);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
