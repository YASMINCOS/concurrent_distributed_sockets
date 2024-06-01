package servidor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Livro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.List;

public class Server {

    private static final String FILE_PATH = "src/main/java/json/livros.json";
    private static List<Livro> livros;

    public static void main(String[] args) {

        carregarLivros();

        try (ServerSocket serverSocket = new ServerSocket(12346)) {
            System.out.println("Servidor iniciado na porta 12345...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("LISTAR")) {
                        enviarListaLivros(out);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void carregarLivros() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(Paths.get(FILE_PATH).toFile());
            JsonNode livrosNode = rootNode.get("livros");
            if (livrosNode != null && livrosNode.isArray()) {livros = objectMapper.convertValue(livrosNode, new TypeReference<List<Livro>>() {});
                System.out.println("Livros carregados com sucesso.");
            } else {
                System.out.println("Erro: Array 'livros' não encontrado no JSON.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar os livros do arquivo JSON.");
        }

    }

    private static void enviarListaLivros(PrintWriter out) {
        if (livros != null) {
            for (Livro livro : livros) {
                out.println(livro);
            }
        }
        out.println("FIM_LISTA");
    }

    private static void adicionarLivros(Livro livro){
        livros.add(livro);
        salvarLivrosNoJson();
    }

    private static void decrementarExemplares(String titulo){
        if (livros != null){
            for (Livro livro : livros){
                if(livro.getTitulo().equalsIgnoreCase(titulo) && livro.getExemplares() > 0){
                    livro.setExemplares(livro.getExemplares() - 1);
                    salvarLivrosNoJson();
                }
            }
        }
    }

    private static void salvarLivrosNoJson(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(FILE_PATH).toFile(), livros);
            System.out.println("Livro salvo.");
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Erro");
        }
    }
}
