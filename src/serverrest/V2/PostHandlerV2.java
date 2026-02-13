package serverrest.V2;

import com.sun.net.httpserver.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.UUID;
import serverrest.OperazioneRequest;

public class PostHandlerV2 implements HttpHandler {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            inviaErrore(exchange, 405, "Metodo non consentito. Usa POST");
            return;
        }

        try {
            // Legge il body JSON
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            OperazioneRequest req = gson.fromJson(body, OperazioneRequest.class);

            if (req == null || req.getOperatore() == null) {
                inviaErrore(exchange, 400, "Payload non valido");
                return;
            }

            double risultato = CalcolatriceServiceV2.calcola(
                    req.getOperando1(),
                    req.getOperando2(),
                    req.getOperatore()
            );

            String requestId = UUID.randomUUID().toString();

            OperazioneResponseV2 resp = new OperazioneResponseV2(
                    req.getOperando1(),
                    req.getOperando2(),
                    req.getOperatore(),
                    risultato,
                    requestId
            );

            inviaRisposta(exchange, 200, gson.toJson(resp));

        } catch (IllegalArgumentException e) {
            inviaErrore(exchange, 400, e.getMessage());
        } catch (Exception e) {
            inviaErrore(exchange, 500, "Errore interno del server: " + e.getMessage());
        }
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
