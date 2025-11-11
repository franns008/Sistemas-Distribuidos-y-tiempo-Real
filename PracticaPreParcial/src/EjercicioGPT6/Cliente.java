package EjercicioGPT6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        String direccion = "localHost";
        int puerto = 6060;
        DataOutputStream toServer;
        DataInputStream fromServer;
        try{
            Socket s = new Socket(direccion,puerto);
            toServer = new DataOutputStream(s.getOutputStream());
            fromServer = new DataInputStream(s.getInputStream());
            long inicio = System.currentTimeMillis();
            String mensaje = "Hola este es un texto largo que simula" +
                    "estar sacado de un archivo"+"\n"+
                    "Aca hay un salto de linea";
            toServer.writeUTF(mensaje);
            String rsp = fromServer.readUTF();
            long tiempoTot = System.currentTimeMillis()-inicio;
            System.out.println("El servidor lo recibio, te mando "+rsp);
            System.out.println("RTT: "+tiempoTot);

        }catch (Exception e){
            System.out.println("Error grave");
        }
    }
}
/*
¿Qué función bloqueante
del servidor se utiliza para aceptar la conexión de un cliente?
El servidor usa accept(), que es la funcion que usa para aceptar una
conexion de un cliente.


¿Qué función usa el cliente para solicitar una conexión?
Para solicitar una conexion, lo que se usa es la funcion new Sokcet.
El constructor establece automaticamente la conexion con el servidor


¿Qué sucede si dos clientes intentan conectarse al mismo puerto del servidor simultáneamente?
Depende de la configuracion (backlog) va a atenderlos simultaneamente o uno se va a quedar esperando




 */
