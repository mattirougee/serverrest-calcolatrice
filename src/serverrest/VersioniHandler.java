package serverrest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersioniHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, String>> versioni = new ArrayList<>();

        // V1
        Map<String, String> v1 = new HashMap<>();
        v1.put("versione", "V1");
        v1.put("stato", "Phased out");
        v1.put("data_dismissione", "28/2/2026");
        versioni.add(v1);

        // V2
        Map<String, String> v2 = new HashMap<>();
        v2.put("versione", "V2");
        v2.put("stato", "Active");
        versioni.add(v2);

        // V3
        Map<String, String> v3 = new HashMap<>();
        v3.put("versione", "V3");
        v3.put("stato", "Current");
        versioni.add(v3);

        responseMap.put("versioni_supportate", versioni);

        String json = gson.toJson(responseMap);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}