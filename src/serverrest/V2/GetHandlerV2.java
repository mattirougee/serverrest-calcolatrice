package serverrest.V2;

import com.sun.net.httpserver.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.UUID;

public class GetHandlerV2 implements HttpHandler {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            inviaErrore(exchange, 405, "Metodo non consentito. Usa GET");
            return;
        }

        try {
            Map<String, String> p = estraiParametri(exchange.getRequestURI().getQuery());

            if (!p.containsKey("operando1") || !p.containsKey("operatore")) {
                inviaErrore(exchange, 400, "Parametri mancanti. Necessari: operando1, operatore");
                return;
            }

            double operando1 = Double.parseDouble(p.get("operando1"));
            double operando2 = p.containsKey("operando2") ? Double.parseDouble(p.get("operando2")) : 0;
            String operatore = p.get("operatore");

            double risultato = CalcolatriceServiceV2.calcola(operando1, operando2, operatore);

            String requestId = UUID.randomUUID().toString();

            OperazioneResponseV2 response = new OperazioneResponseV2(
                    operando1, operando2, operatore, risultato, requestId
            );

            inviaRisposta(exchange, 200, gson.toJson(response));

        } catch (NumberFormatException e) {
            inviaErrore(exchange, 400, "Operandi non validi. Devono essere numeri");
        } catch (IllegalArgumentException e) {
            inviaErrore(exchange, 400, e.getMessage());
        } catch (Exception e) {
            inviaErrore(exchange, 500, "Errore interno del server: " + e.getMessage());
        }
    }

    private Map<String,String> estraiParametri(String query) {
        Map<String,String> m = new HashMap<>();
        if (query == null) return m;
        for (String p : query.split("&")) {
            String[] kv = p.split("=");
            if (kv.length == 2) {
                m.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                      URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return m;
    }

    private void inviaRisposta(HttpExchange ex, int code, String body) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] b = body.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, b.length);
        ex.getResponseBody().write(b);
        ex.getResponseBody().close();
    }

    private void inviaErrore(HttpExchange ex, int code, String msg) throws IOException {
        Map<String,Object> err = new HashMap<>();
        err.put("errore", msg);
        err.put("status", code);
        inviaRisposta(ex, code, gson.toJson(err));
    }
}
