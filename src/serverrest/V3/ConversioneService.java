package serverrest.V3;

import serverrest.V3.*;

public class ConversioneService {

    private static final double FATTORE_CONVERSIONE = 1.09361; // 1 metro = 1.09361 yard

    public static double converti(String unita1, String unita2, double valore) {
        String u1 = unita1.toLowerCase().trim();
        String u2 = unita2.toLowerCase().trim();

        if (u1.equals("mt") && u2.equals("yd")) {
            return valore * FATTORE_CONVERSIONE;
        } else if (u1.equals("yd") && u2.equals("mt")) {
            return valore / FATTORE_CONVERSIONE;
        } else {
            throw new IllegalArgumentException("Conversione non supportata. Usa 'mt' e 'yd'");
        }
    }
}