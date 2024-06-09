package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args) {
        try {
            Conexao conexao = new Conexao("localhost", 12346);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                menu();
                int operacao = Integer.parseInt(console.readLine());
                if (operacao == 0) break;
                switch (operacao) {
                    case 1:
                        enviarLivros(conexao, console);
                        break;
                    case 2:
                        alugarLivro(conexao, console);
                        break;
                    case 3:
                        devolverLivro(conexao, console);
                        break;
                    case 4:
                        cadastrarLivros(conexao, console);
                        break;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            }
            conexao.fechar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cadastrarLivros(Conexao conexao, BufferedReader console) throws IOException{
        System.out.println("Digite o título do livro:");
        String titulo = console.readLine();
        System.out.println("Digite o autor do livro:");
        String autor = console.readLine();
        System.out.println("Digite o gênero do livro:");
        String genero = console.readLine();
        System.out.println("Digite a quantidade de exemplares:");
        int exemplares = Integer.parseInt(console.readLine());
        String comando = String.format("CADASTRAR %s, %s, %s, %d", titulo, autor, genero, exemplares);
        System.out.println("Enviando comando: " + comando);
        conexao.enviar(comando);
    }

    public static void devolverLivro(Conexao conexao, BufferedReader console) throws IOException{
        System.out.println("Digite o nome do livro que deseja devolver: ");
        String tituloDevolver = console.readLine();
        String comandoDevolver = String.format("DEVOLVER %s", tituloDevolver);
        System.out.println("Enviando comando: " + comandoDevolver);
        conexao.enviar(comandoDevolver);
    }

    public static void alugarLivro(Conexao conexao, BufferedReader console) throws IOException {
        System.out.println("Digite o nome do livro que deseja alugar: ");
        String tituloAlugar = console.readLine();
        String comandoAlugar = String.format("ALUGAR %s", tituloAlugar);
        System.out.println("Enviando comando: " + comandoAlugar);
        conexao.enviar(comandoAlugar);
        String respostaAluguel = conexao.receber();
        if (respostaAluguel.startsWith("ALERTA:")) {
            System.out.println(respostaAluguel);
        } else {
            System.out.println(respostaAluguel);
        }
    }

    public static void enviarLivros(Conexao conexao, BufferedReader console) throws IOException {
        conexao.enviar("LISTAR");
        String resposta;
        while (!(resposta = conexao.receber()).equals("FIM_LISTA")) {
            System.out.println(resposta);
        }
    }

    public static void menu() {
        System.out.println("====== MENU ======");
        System.out.println("1 - LISTAR");
        System.out.println("2 - ALUGAR");
        System.out.println("3 - DEVOLVER");
        System.out.println("4 - CADASTRAR");
        System.out.println("0 - SAIR");
        System.out.println("==================");
        System.out.println("Opção:");
    }
}
