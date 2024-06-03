package servidor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    private static void alugarLivro(String nomeLivro,  PrintWriter out) {
        try {
            System.out.println("Tentando alugando livro: " + nomeLivro);
            String[] partes = nomeLivro.split(" ");
            // separando o retorno e pegando o ultimo index - representa o titulo do livro
            String titulo = partes[partes.length - 1].trim();
            ObjectMapper mapper = new ObjectMapper();
            // puxando os dados do json
            JsonNode rootNode = mapper.readTree(Paths.get(FILE_PATH).toFile());
            System.out.println("aaaaaaaAAAAAAAA");
            // iterando todos os livros e procurando o nome que o cliente deseja alugar
            for (JsonNode node : rootNode) {
                if (node.get("titulo").asText().equals(titulo)) {
                    // tranformando o node em Object node para permitir modificacoes;
                    ObjectNode objectNode = (ObjectNode) node;
                    int numeroExemplaresDispiveis = objectNode.get("exemplares").asInt();
                    // caso o numero de exemplares for maior que 0 então ainda há livros para alugar
                    if (numeroExemplaresDispiveis > 0) {
                        objectNode.put("exemplares", numeroExemplaresDispiveis - 1);
                        out.println("Livro alugado com sucesso!");
                    } else {
                        out.println("Não há exemplares desse livro disponiveis para aluguel no momento.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao alugar os livros - lista vazio.");
        }
    }

    private static void devolverLivro(String nomeLivro,  PrintWriter out) {
        try {
            System.out.println("tentando devolvendo livro: " + nomeLivro);
            String[] partes = nomeLivro.split(" ");
            // separando o retorno e pegando o ultimo index - representa o titulo do livro
            String titulo = partes[partes.length - 1].trim();
            ObjectMapper mapper = new ObjectMapper();
            // puxando os dados do json
            JsonNode rootNode = mapper.readTree(Paths.get(FILE_PATH).toFile());
            System.out.println("aaaaaaaAAAAAAAA");
            // iterando todos os livros e procurando o nome que o cliente deseja devolver
            for (JsonNode node : rootNode) {
                if (node.get("titulo").asText().equals(titulo)) {
                    // tranformando o node em Object node para permitir modificacoes;
                    ObjectNode objectNode = (ObjectNode) node;
                    int numeroExemplaresDispiveis = objectNode.get("exemplares").asInt();
                    if (numeroExemplaresDispiveis > 0) { // TODO: mudar a logica de devolucao
                        objectNode.put("exemplares", numeroExemplaresDispiveis + 1);
                        out.println("Livro devolvido com sucesso!");
                    } else {
                        out.println("Não há exemplares desse livro a serem devolvidos.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao alugar os livros - lista vazio.");
        }
    }
}
