package sae.statisalle;

public class Session {
    private static String adresseIp;
    private static Reseau reseau;

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
}