package org.ghiorsi;

import java.io.Serializable;
import java.util.ArrayList;

public class ShippingPackage implements Serializable {

    /**
     * This class will be i charge of send the information to the Server, for this, it needs to implement Serializable
     */

    private String nick, ip, mensaje;

    private ArrayList<String> Ips;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ArrayList<String> getIps() {
        return Ips;
    }

    public void setIps(ArrayList<String> ips) {
        Ips = ips;
    }
}
