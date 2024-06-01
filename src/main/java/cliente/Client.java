package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args) {
        try {
            Conexao conexao = new Conexao("localhost", 12346);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            menu();
            while (true) {
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
                        System.out.println("Não operacional");
                        break;
                    default:
                        System.out.println("Opção invalida");
                }
                System.out.println(conexao.receber());
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

