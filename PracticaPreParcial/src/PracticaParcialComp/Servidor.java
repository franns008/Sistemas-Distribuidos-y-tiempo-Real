package PracticaParcialComp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/*
Implemente en C un sistema cliente–servidor denominado PingPong. Servidor: escucha
en el puerto 5000 y responde con el mensaje "pong" cada vez que recibe "ping". Cliente:

se conecta al servidor, env ́ıa el mensaje "ping" y muestra la respuesta recibida. Utili-
ce las llamadas est ́andar de Berkeley Sockets (socket(), bind(), listen(), accept(),

connect(), read(), write(), close()) y describa brevemente el flujo de ejecuci ́on.
*/


public class Servidor {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Faltan argumentos: port");
        }

        int port = 5000;

        try {
            // Bind + Listen
            ServerSocket socket = new ServerSocket(port);


            // While de cliente
            while (true) {
                int count = 0;
                Socket clientSock = socket.accept();

                // While de 3 errores
                while (count < 3) {
                    DataInputStream fromClient = new DataInputStream(clientSock.getInputStream());
                    DataOutputStream toClient = new DataOutputStream(clientSock.getOutputStream());

                    try {
                        String respuesta = fromClient.readUTF();
                        System.out.println("me llego "+respuesta);
                        if(respuesta.equals("ping")){
                            toClient.writeUTF("pong");
                        } else {
                            count++;
                        }
                    }catch (Exception e){
                        break;
                    }


                }

                if (count == 3) {
                    break;
                }
            }
            socket.close();
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
