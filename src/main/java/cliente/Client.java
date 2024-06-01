package cliente;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        try {
            Conexao conexao = new Conexao("localhost", 12346);
            System.out.println("Conectado ao servidor!");

            conexao.enviar("LISTAR");
            String responseLine;
            while ((responseLine = conexao.receber()) != null) {
                if (responseLine.equals("FIM_LISTA")) {
                    break;
                }
                System.out.println(responseLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

