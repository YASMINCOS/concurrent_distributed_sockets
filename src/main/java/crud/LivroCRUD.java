package crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Livro;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LivroCRUD {

    private static final String FILE_PATH = "src/main/java/json/livros.json";
    private static List<Livro> livros = new ArrayList<>();

    static {
        carregarLivros();
    }

    private static void carregarLivros() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Livro[] livrosArray = objectMapper.readValue(Paths.get(FILE_PATH).toFile(), Livro[].class);
            livros = new ArrayList<>(Arrays.asList(livrosArray));
            System.out.println("Livros carregados com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar os livros do arquivo JSON. Inicializando lista vazia.");
            livros = new ArrayList<>();
        }
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

    public static void enviarListaLivros(PrintWriter out) {
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

    public static void cadastrarLivro(String livroStr, PrintWriter out) {
        try {
            System.out.println("Cadastrando livro: " + livroStr);
            String[] partes = livroStr.split(",");
            if (partes.length == 4) {
                String titulo = partes[0].trim();
                String autor = partes[1].trim();
                String genero = partes[2].trim();
                int exemplares = Integer.parseInt(partes[3].trim());

                Livro novoLivro = new Livro(titulo, autor, genero, exemplares);
                livros.add(novoLivro);
                salvarLivrosNoJson();
                out.println("Livro cadastrado com sucesso!");
            } else {
                out.println("Erro: Formato inválido. Use: CADASTRAR titulo, autor, genero, exemplares");
                return;
            }
        } catch (NumberFormatException e) {
            out.println("Erro: Número de exemplares inválido.");
        }
    }

    public static void alugarLivro(String nomeLivro, PrintWriter out) throws IOException {
        boolean livroEncontrado = false;
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(nomeLivro)) {
                livroEncontrado = true;
                if (livro.getExemplares() > 0) {
                    livro.setExemplares(livro.getExemplares() - 1);
                    salvarLivrosNoJson();
                    out.println("Livro alugado com sucesso!");
                    return;
                } else {
                    out.println("ALERTA: Não há exemplares desse livro disponíveis para aluguel no momento.");
                    return;
                }
            }
        }
        if (!livroEncontrado) {
            out.println("Livro não encontrado.");
        }
    }

    public static void devolverLivro(String nomeLivro, PrintWriter out) throws IOException {
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
