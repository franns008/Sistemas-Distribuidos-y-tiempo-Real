package EjercicioGPT1;
/*
Enunciado:
Implemente en C un sistema cliente–servidor denominado EcoMedido.
El servidor debe:

Escuchar en el puerto 5050.

Recibir mensajes del cliente y devolverlos precedidos por la palabra "Eco:".

Medir y mostrar el tiempo promedio entre la recepción y el envío (latencia interna).
 */


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class Cliente {

    public static void main(String[] args) {
        int puerto = 5050;
        String direccion = "127.0.0.1";
        DataInputStream fromServer;

        DataOutputStream toServer;
        String mensaje;
        String respuestaServidor;
        byte[] Respuesta = new byte[451];
        long  tiempoInicio;
        try {
            Socket socketClient = new Socket(direccion,puerto);
            toServer=new DataOutputStream(socketClient.getOutputStream());
            fromServer = new DataInputStream(socketClient.getInputStream());
            for (int i = 1; i <= 5 ; i++) {
                tiempoInicio= System.currentTimeMillis();
                mensaje = "Hola servidor, este es el mensaje"+i;
                toServer.writeUTF(mensaje);
                System.out.println("Respuesta: "+fromServer.readUTF());
            }
            System.out.println("Fin de la comunicaicon");
            socketClient.close();
        }catch (Exception e){

        }

    }
}
