package serverrest.V3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PostHandlerV3 implements HttpHandler {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            inviaRisposta(exchange, 405, gson.toJson(Map.of("errore", "Metodo non consentito")));
            return;
        }

        try {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            ConversioneRequest req = gson.fromJson(body, ConversioneRequest.class);

            double risultato = ConversioneService.converti(req.getUnita1(), req.getUnita2(), req.getValore());

            ConversioneResponse response = new ConversioneResponse(req.getUnita1(), req.getUnita2(), req.getValore(), risultato);
            inviaRisposta(exchange, 200, gson.toJson(response));

        } catch (Exception e) {
            inviaRisposta(exchange, 500, gson.toJson(Map.of("errore", e.getMessage())));
        }
    }

    private void inviaRisposta(HttpExchange exchange, int code, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}