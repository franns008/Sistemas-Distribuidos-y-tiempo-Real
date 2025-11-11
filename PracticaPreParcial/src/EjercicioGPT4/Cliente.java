package EjercicioGPT4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        int port = 5051;
        String direccion = "LocalHost";
        DataInputStream fromServer;
        DataOutputStream toServer;
        try{
            Socket s = new Socket(direccion,port);
            fromServer = new DataInputStream(s.getInputStream());
            toServer = new DataOutputStream(s.getOutputStream());
            int N = 545234;

            long inicio = System.currentTimeMillis();
            toServer.writeInt(N);
            String respuesta = fromServer.readUTF();
            long tiempoTot = System.currentTimeMillis() - inicio;
            System.out.println("Respuesta: "+respuesta);
            System.out.println("RTT: "+tiempoTot);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
