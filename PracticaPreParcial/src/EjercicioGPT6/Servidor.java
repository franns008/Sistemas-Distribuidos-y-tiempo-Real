package EjercicioGPT6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        int port =6060;
        DataInputStream fromClient;
        DataOutputStream toClient;
        try {
            ServerSocket server = new ServerSocket(port);
            Socket client = server.accept();
            fromClient = new DataInputStream(client.getInputStream());
            toClient = new DataOutputStream(client.getOutputStream());
            String mensaje = fromClient.readUTF();
            FileWriter file = new FileWriter("log.txt");
            file.write(mensaje);
            System.out.println("Recibi el mensaje, mira el archivo log.txt");
            toClient.writeUTF("Recibido");
        }catch (Exception e){
            System.out.println("Error grave");
        }

    }
}
