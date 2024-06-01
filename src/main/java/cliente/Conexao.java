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
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void enviar(String msg) {
        out.println(msg);
    }

    public String receber() throws IOException {
        return in.readLine();
    }

    public void fechar() throws IOException {
        socket.close();
        in.close();
        out.close();
    }
}
