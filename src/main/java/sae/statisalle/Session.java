package sae.statisalle;

import java.net.Socket;

public class Session {
    private static String adresseIp;
    private static String contenu;
    private static Reseau reseau;
    private static String cle;

    public static String getAdresseIp() {
        return adresseIp;
    }

    public static void setAdresseIp(String ip) {
        adresseIp = ip;
    }

    public static Reseau getReseau() {
        return reseau;
    }

    public static void setReseau(Reseau reseau) {
        Session.reseau = reseau;
    }

    public static String getContenu() {
        return contenu;
    }

    public static void setContenu(String contenu) {
        Session.contenu = contenu;
    }

    public static void setCle(String cle) {
        Session.cle = cle;
    }

    public static String getCle() {
        return cle;
    }
}