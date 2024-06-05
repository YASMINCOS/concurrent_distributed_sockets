package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Conexao {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Conexao(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void enviar(String mensagem) {
        out.println(mensagem);
    }

    public String receber() throws IOException {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Erro ao receber dados: " + e.getMessage());
            throw e;
        }
    }

    public void fechar() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
