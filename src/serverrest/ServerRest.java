package serverrest;

// Import V1
import serverrest.V1.GetHandlerV1;
import serverrest.V1.PostHandlerV1;
// Import V2
import serverrest.V2.GetHandlerV2;
import serverrest.V2.PostHandlerV2;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import serverrest.V3.GetHandlerV3;
import serverrest.V3.PostHandlerV3;

/**
 * Server REST per la calcolatrice e convertitore
 * 
 *
 * @author delfo
 */
public class ServerRest {

    /**
     * Avvia il server REST sulla porta specificata
     */
    public static void avviaServer(int porta) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);

            // LEGACY
            server.createContext("/api/calcola/get", new GetHandlerV1());
            server.createContext("/api/calcola/post", new PostHandlerV1());

            // V1
            server.createContext("/api/v1/calcola/get", new GetHandlerV1());
            server.createContext("/api/v1/calcola/post", new PostHandlerV1());

            // V2
            server.createContext("/api/v2/calcola/get", new GetHandlerV2());
            server.createContext("/api/v2/calcola/post", new PostHandlerV2());

            // V3
            server.createContext("/api/v3/converte/get", new GetHandlerV3());
            server.createContext("/api/v3/converte/post", new PostHandlerV3());

            // Gestione Versioni
            server.createContext("/api/versioni", new VersioniHandler());

            // Benvenuto
            server.createContext("/", ServerRest::gestisciBenvenuto);

            server.setExecutor(null);
            server.start();

            System.out.println("==============================================");
            System.out.println("  Server REST Calcolatrice - API V1, V2 & V3");
            System.out.println("==============================================");
            System.out.println("Porta: " + porta);
            System.out.println();
            System.out.println("Endpoint disponibili:");
            
            System.out.println("V1 (Calcolatrice Base)");
            System.out.println("  - GET: http://localhost:" + porta + "/api/v1/calcola/get");
            System.out.println("  - POST:  http://localhost:" + porta + "/api/v1/calcola/post");
            
            System.out.println("V2 (Calcolatrice Scientifica)");
            System.out.println("  - GET: http://localhost:" + porta + "/api/v2/calcola/get");
            System.out.println("  - POST:  http://localhost:" + porta + "/api/v2/calcola/post");
            
            System.out.println("V3 (Convertitore)");
            System.out.println("  - GET: http://localhost:" + porta + "/api/v3/converte/get");
            System.out.println("  - POST:  http://localhost:" + porta + "/api/v3/converte/post");
            
            System.out.println("INFO");
            System.out.println("  - Versioni: http://localhost:" + porta + "/api/versioni");
            System.out.println("  - Home:     http://localhost:" + porta + "/");
            System.out.println();

        } catch (IOException e) {
            System.err.println("Errore nell'avvio del server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Endpoint di benvenuto con info API
     */
    private static void gestisciBenvenuto(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Map<String, Object> info = new HashMap<>();
        info.put("messaggio", "Benvenuto alla Calcolatrice REST API");
        info.put("versione_api", "v1, v2, v3");
        info.put("tecnologia", "Java + GSON");
        info.put("url_versioni", "/api/versioni");

        Map<String, Object> allEndpoints = new HashMap<>();

        // Endpoints V1
        Map<String, String> endpointsV1 = new HashMap<>();
        endpointsV1.put("get", "/api/v1/calcola/get?operando1=X&operando2=Y&operatore=OP");
        endpointsV1.put("post", "/api/v1/calcola/post");
        allEndpoints.put("v1", endpointsV1);

        // Endpoints V2
        Map<String, String> endpointsV2 = new HashMap<>();
        endpointsV2.put("get", "/api/v2/calcola/get?operando1=X&operando2=Y&operatore=OP");
        endpointsV2.put("post", "/api/v2/calcola/post");
        allEndpoints.put("v2", endpointsV2);

        // Endpoints V3 (NUOVO)
        Map<String, String> endpointsV3 = new HashMap<>();
        endpointsV3.put("get", "/api/v3/converte/get?unita1=mt&unita2=yd&valore=X");
        endpointsV3.put("post", "/api/v3/converte/post");
        allEndpoints.put("v3", endpointsV3);

        info.put("endpoints", allEndpoints);

        Map<String, String> operatori = new HashMap<>();
        operatori.put("calcolatrice", "SOMMA, SOTTRAZIONE, MOLTIPLICAZIONE, DIVISIONE, POTENZA, RADICE");
        operatori.put("convertitore", "mt (metri), yd (yard)");
        info.put("operatori_supportati", operatori);

        String json = gson.toJson(info);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}