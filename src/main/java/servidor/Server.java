package servidor;

import crud.LivroCRUD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(12346)) {
            System.out.println("Servidor iniciado na porta 12346...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    gerenciarInput(in, out);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void gerenciarInput(BufferedReader in, PrintWriter out) throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.equals("LISTAR")) {
                LivroCRUD.enviarListaLivros(out);
            } else if (inputLine.startsWith("CADASTRAR")) {
                LivroCRUD.cadastrarLivro(inputLine.substring("CADASTRAR".length()).trim(), out);
            } else if (inputLine.startsWith("ALUGAR")) {
                LivroCRUD.alugarLivro(inputLine.substring("ALUGAR".length()).trim(), out);
            } else if (inputLine.startsWith("DEVOLVER")) {
                LivroCRUD.devolverLivro(inputLine.substring("DEVOLVER".length()).trim(), out);
            }
        }
    }
}
