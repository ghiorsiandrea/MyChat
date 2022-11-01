package org.ghiorsi.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

class LaminaMarcoCliente extends JPanel {
    private final JTextField campoDeMensaje;
    private final JComboBox<String> nicks;
    private final JLabel myNick;
    private final JTextArea campochat;
    private final JButton sendButton;

    public Sender mySender;

    public LaminaMarcoCliente(String userNick, Sender mySender) {

        this.mySender = mySender;

        myNick = new JLabel();
        myNick.setText(userNick);
        myNick.setBackground(java.awt.Color.CYAN);
        add(myNick);

        JLabel texto = new JLabel(" - Online - ");
        texto.setBackground(java.awt.Color.cyan);
        add(texto);

        nicks = new JComboBox<>();
        add(nicks);
        nicks.setBackground(java.awt.Color.cyan);

        campochat = new JTextArea(12, 20);
        add(campochat);
        campochat.setBackground(java.awt.Color.cyan);

        campoDeMensaje = new JTextField(20);
        add(campoDeMensaje);
        campoDeMensaje.setBackground(java.awt.Color.cyan);

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
            String nickReceiverText = (String) nicks.getSelectedItem();
            String nickSenderText = myNick.getText();
            String message = campoDeMensaje.getText();

            System.out.println("\n" + nickSenderText + ": " + message);
            campochat.append("\n" + nickSenderText + ": " + message);
            try {
                mySender.sendMessage(nickReceiverText, nickSenderText, message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
