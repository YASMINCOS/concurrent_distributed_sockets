package sockets.servidor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sockets.model.Livro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.List;

public class Server {

    private static final String FILE_PATH = "sockets/json/livros.json";
    private static List<Livro> livros;

    public static void main(String[] args) {

        carregarLivros();

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
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
            livros = objectMapper.readValue(Paths.get(FILE_PATH).toFile(), new TypeReference<List<Livro>>() {
            });
            System.out.println("Livros carregados com sucesso.");
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
}
