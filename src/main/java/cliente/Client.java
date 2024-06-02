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
                int op = Integer.parseInt(console.readLine());
                if (op == 0) break;
                switch (op) {
                    case 1:
                        conexao.enviar("LISTAR");
                        break;
                    case 2:
                        System.out.println("Não operacional");
                        break;
                    case 3:
                        System.out.println("Não operacional");
                        break;
                    case 4:
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
                        break;
                    default:
                        System.out.println("Opção inválida");
                }
                String resposta = conexao.receber();
                System.out.println("Resposta do servidor: " + resposta);
            }
            conexao.fechar();
        } catch (IOException e) {
            e.printStackTrace();
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
