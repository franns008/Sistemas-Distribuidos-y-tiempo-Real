package EjercicioGPT4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Servidor {

    public static void main(String[] args) {
        DataOutputStream toClient;
        DataInputStream fromClient;
        try {
            ServerSocket server = new ServerSocket(5051);
            Socket client = server.accept();
            toClient = new DataOutputStream(client.getOutputStream());
            fromClient = new DataInputStream(client.getInputStream());
            int N = fromClient.readInt();
            String ack = "AKC";
            toClient.writeUTF(ack);
            System.out.println("N: "+N);
            TimeUnit.SECONDS.sleep(5);
            System.out.println("Voy a cerrar la conexion");
            client.close();
        }catch (Exception e){

        }

    }
}
