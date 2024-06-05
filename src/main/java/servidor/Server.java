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
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final String FILE_PATH = "src/main/java/json/livros.json";
    private static List<Livro> livros = new ArrayList<>();

    public static void main(String[] args) {

        carregarLivros();

        try (ServerSocket serverSocket = new ServerSocket(12346)) {
            System.out.println("Servidor iniciado na porta 12346...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Recebido: " + inputLine);
                        if (inputLine.equals("LISTAR")) {
                            enviarListaLivros(out);
                        } else if (inputLine.startsWith("CADASTRAR")) {
                            cadastrarLivro(inputLine.substring("CADASTRAR".length()).trim(), out);
                        } else if (inputLine.startsWith("ALUGAR")) {
                            alugarLivro(inputLine.substring("ALUGAR".length()).trim(), out);
                        } else if (inputLine.startsWith("DEVOLVER")) {
                            devolverLivro(inputLine.substring("DEVOLVER".length()).trim(), out);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
            if (livrosNode != null && livrosNode.isArray()) {
                livros = objectMapper.convertValue(livrosNode, new TypeReference<List<Livro>>() {});
                System.out.println("Livros carregados com sucesso.");
            } else {
                System.out.println("Erro: Array 'livros' não encontrado no JSON. Inicializando lista vazia.");
                livros = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar os livros do arquivo JSON. Inicializando lista vazia.");
            livros = new ArrayList<>();
        }
    }

    private static void enviarListaLivros(PrintWriter out) {
        if (livros != null && !livros.isEmpty()) {
            out.println("Lista de Livros:");
            for (int i = 0; i < livros.size(); i++) {
                Livro livro = livros.get(i);
                out.println((i + 1) + ". " + livro.getTitulo() + " - " + livro.getAutor() + " - " + livro.getGenero() + " - Exemplares: " + livro.getExemplares());
            }
        } else {
            out.println("Não há livros cadastrados.");
        }
        out.println("FIM_LISTA");
    }

    private static void cadastrarLivro(String livroStr, PrintWriter out) {
        try {
            System.out.println("Cadastrando livro: " + livroStr);
            String[] partes = livroStr.split(",");
            if (partes.length == 4) {
                String titulo = partes[0].trim();
                String autor = partes[1].trim();
                String genero = partes[2].trim();
                int exemplares = Integer.parseInt(partes[3].trim());

                Livro novoLivro = new Livro(titulo, autor, genero, exemplares);
                adicionarLivros(novoLivro);
                out.println("Livro cadastrado com sucesso!");
            } else {
                out.println("Erro: Formato inválido. Use: CADASTRAR titulo, autor, genero, exemplares");
            }
        } catch (NumberFormatException e) {
            out.println("Erro: Número de exemplares inválido.");
        }
    }

    private static void adicionarLivros(Livro livro) {
        livros.add(livro);
        salvarLivrosNoJson();
    }

    private static void salvarLivrosNoJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(FILE_PATH).toFile(), livros);
            System.out.println("Livro salvo.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar os livros no arquivo JSON.");
        }
    }

    private static void alugarLivro(String nomeLivro, PrintWriter out) throws IOException {
        System.out.println("Tentando alugar livro: " + nomeLivro);
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(nomeLivro)) {
                if (livro.getExemplares() > 0) {
                    livro.setExemplares(livro.getExemplares() - 1);
                    salvarLivrosNoJson();
                    out.println("Livro alugado com sucesso!");
                    return;
                } else {
                    out.println("Não há exemplares desse livro disponíveis para aluguel no momento.");
                    return;
                }
            }
        }
        out.println("Livro não encontrado.");
    }

    private static void devolverLivro(String nomeLivro, PrintWriter out) throws IOException {
        System.out.println("Tentando devolver livro: " + nomeLivro);
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(nomeLivro)) {
                livro.setExemplares(livro.getExemplares() + 1);
                salvarLivrosNoJson();
                out.println("Livro devolvido com sucesso!");
                return;
            }
        }
        out.println("Livro não encontrado.");
    }
}
