package serverrest.V3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetHandlerV3 implements HttpHandler {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            inviaRisposta(exchange, 405, gson.toJson(Map.of("errore", "Metodo non consentito")));
            return;
        }

        try {
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            
            if (!params.containsKey("unita1") || !params.containsKey("unita2") || !params.containsKey("valore")) {
                inviaRisposta(exchange, 400, gson.toJson(Map.of("errore", "Parametri mancanti")));
                return;
            }

            String u1 = params.get("unita1");
            String u2 = params.get("unita2");
            double val = Double.parseDouble(params.get("valore"));

            double risultato = ConversioneService.converti(u1, u2, val);

            ConversioneResponse response = new ConversioneResponse(u1, u2, val, risultato);
            inviaRisposta(exchange, 200, gson.toJson(response));

        } catch (Exception e) {
            inviaRisposta(exchange, 500, gson.toJson(Map.of("errore", e.getMessage())));
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(URLDecoder.decode(entry[0], StandardCharsets.UTF_8), 
                           URLDecoder.decode(entry[1], StandardCharsets.UTF_8));
            }
        }
        return result;
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