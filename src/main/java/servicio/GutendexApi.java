package servicio;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GutendexApi {

    private HttpClient cliente;
    private HttpRequest request;

    public GutendexApi(){
        this.cliente =  HttpClient.newHttpClient();
    }

    public String obtenerLibro(String param) throws IOException, InterruptedException {
        this.request = HttpRequest.newBuilder()
                .uri(URI.create("https://gutendex.com/books?search="+param.replace(" ", "%20")))
                .build();

        HttpResponse<String> respuesta = cliente.send(request, HttpResponse.BodyHandlers.ofString());

        return respuesta.body();
    }
}
