package EjercicioGPT1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        DataOutputStream toClient;
        DataInputStream fromClient;
        String mensajeRecibido;
        String mensajeRespuesta;
        try{
            ServerSocket servidor = new ServerSocket(5050);
            Socket cliente = servidor.accept();
            System.out.println("Acepte una conexion");
            toClient = new DataOutputStream(cliente.getOutputStream());
            fromClient = new DataInputStream(cliente.getInputStream());
            while(cliente.isConnected()){
                mensajeRecibido = fromClient.readUTF();
                System.out.println("recibi el mensaje "+mensajeRecibido);
                mensajeRespuesta = "Eco: "+mensajeRecibido;
                toClient.writeUTF(mensajeRespuesta);
            }
            System.out.println("Se termino la conexion");
        }catch (Exception e){
            System.out.println("Error en la conexion");
        }
    }
}
