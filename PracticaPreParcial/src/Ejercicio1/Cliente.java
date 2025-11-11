package Ejercicio1;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        InputStream fromSever;
        OutputStream toServer;
        try {
            Socket server = new Socket("LocalHost", 5000);

            toServer = server.getOutputStream();
            String mensaje = "Ping ";
            toServer.write(mensaje.getBytes());
            fromSever = server.getInputStream();

            byte[] respuesta = new byte[4555];

            fromSever.read(respuesta);
            System.out.println("Respuesta del servidor  "+new String(respuesta));
        }catch (Exception e){
            System.out.println("Error en la creacion del servidor");
        }
    }
}
