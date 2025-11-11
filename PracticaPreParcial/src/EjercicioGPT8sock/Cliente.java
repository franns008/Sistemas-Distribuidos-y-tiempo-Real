package EjercicioGPT8sock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        DataInputStream fromServer;
        DataOutputStream toServer;
        int port = 7070;
        String direccion = "localHost";
        try{
            Socket s = new Socket(direccion,port);
            s.setSoTimeout(2111); //agregas el timeOut
            fromServer = new DataInputStream(s.getInputStream());
            toServer = new DataOutputStream(s.getOutputStream());
            for (int i = 0; i <3 ; i++) {
                long inicio = System.currentTimeMillis();
                toServer.writeUTF("Una cadena v1");
                String mensajeVuelta = fromServer.readUTF();
                long fin = System.currentTimeMillis()-inicio;
                System.out.println("Recibi: "+mensajeVuelta);
                System.out.println("RTT: "+fin);
            }
            s.close();

        }catch (Exception e){
            System.out.println("Error en el cliente");
        }
    }
}

/*
¿Qué función del lado del servidor crea el socket de escucha?
La funcion del lado del servidor que crea el socket de escucha es new ServerSocket();

¿Para qué sirve SO_REUSEADDR?
SO_REUSEADDR permite reutilizar una dirección y puerto que estuvieron recientemente en uso,
evitando el error Address already in use al reiniciar el servidor.

¿Cómo se puede evitar que el cliente quede bloqueado indefinidamente si el servidor no responde?
Se le agrega un timeout a la funcion .readUTF().

 */