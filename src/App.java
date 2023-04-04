import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {

        String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
        URI endereco = URI.create(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        var parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);

        var diretorio = new File("figurinhas/");
        diretorio.mkdir();

        var geradora = new GeradoraDeFigurinhas();
        for (int index = 0; index < 5; index++) {
            var filme = listaDeFilmes.get(index);

            String urlImagem = filme.get("image");
            String titulo = filme.get("title");


            InputStream inputStream = new URL(urlImagem).openStream();
            String nomeArquivo = "figurinhas/"+ titulo + ".png";
            double classificacao = Double.parseDouble(filme.get("imDbRating"));

            String textoFigurinha;
            InputStream imagemMarcos;
            if (classificacao >= 8.0) {
                textoFigurinha = "TOPZERA";
                imagemMarcos = new FileInputStream( new File("sobreposicao/muitobom.jpg"));
            } else {
                textoFigurinha = "Hummm...";
                imagemMarcos = new FileInputStream(new File("sobreposicao/maisoumenos.jpg"));
            }

            geradora.cria(inputStream, nomeArquivo, textoFigurinha, imagemMarcos);

            System.out.println(filme.get("title"));
            System.out.println();



        }
            
    }

}