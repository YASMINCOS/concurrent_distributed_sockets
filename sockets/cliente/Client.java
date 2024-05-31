

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 12346)) {
            System.out.println("Conectado ao servidor!");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("LISTAR");

            String responseLine;
            while ((responseLine = in.readLine()) != null) {
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
