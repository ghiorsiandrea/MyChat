package org.ghiorsi.commons;

import java.io.Serializable;
import java.util.ArrayList;

public class ShippingPackage implements Serializable {

    /**
     * This class will be i charge of send the information to the Server, for this, it needs to implement Serializable
     */

    private String nickTo, nickFrom, mensaje;

    private ArrayList<String> nicks;

    public String getNickTo() {
        return nickTo;
    }

    public void setNickTo(String nickTo) {
        this.nickTo = nickTo;
    }

    public String getNickFrom() {
        return nickFrom;
    }

    public void setNickFrom(String nickFrom) {
        this.nickFrom = nickFrom;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ArrayList<String> getNicks() {
        return nicks;
    }

    public void setNicks(ArrayList<String> nicks) {
        this.nicks = nicks;
    }
}

