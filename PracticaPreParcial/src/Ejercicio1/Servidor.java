package Ejercicio1;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        OutputStream toClient;
        InputStream fromClient;
        try{
            ServerSocket servidor = new ServerSocket(5000);
            while (true){
                Socket socket_cliente = servidor.accept();
                fromClient = socket_cliente.getInputStream();
                byte[] datos = new byte[1024];

                int bytesLeidos = fromClient.read(datos);

                String recibi = new String (datos, 0, bytesLeidos);

                System.out.println("El cliente me envio "+recibi);
                toClient = socket_cliente.getOutputStream();
                String respuesta = "pong";
                toClient.write(respuesta.getBytes());
                socket_cliente.close();
            }
        }catch (Exception e){
            System.out.println("Hubo un error al iniciar el servidor: " + e.getMessage());

        }
    }
}
