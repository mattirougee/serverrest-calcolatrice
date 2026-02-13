package serverrest.V3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ConversioneResponse {
    private String unita1;
    private String unita2;
    private double valore_originale;
    private double valore_convertito;
    private String conversione;
    private String timestamp;
    private String versione_api;
    private String request_id;

    public ConversioneResponse(String unita1, String unita2, double valOrig, double valConv) {
        this.unita1 = unita1;
        this.unita2 = unita2;
        this.valore_originale = valOrig;
        this.valore_convertito = Math.round(valConv * 10000.0) / 10000.0; // Arrotondamento a 4 decimali

        // Formattazione stringa "100.0000 mt = 109.3610 yd"
        this.conversione = String.format("%.4f %s = %.4f %s", valOrig, unita1, this.valore_convertito, unita2);

        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.versione_api = "3.0";
        this.request_id = UUID.randomUUID().toString();
    }
}