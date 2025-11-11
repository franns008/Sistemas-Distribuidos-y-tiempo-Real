package PracticaParcialComp;


//Implemente en C un sistema cliente–servidor denominado PingPong. Servidor: escucha
//en el puerto 5000 y responde con el mensaje "pong" cada vez que recibe "ping". Cliente:
//
//se conecta al servidor, env ́ıa el mensaje "ping" y muestra la respuesta recibida. Utili-
//ce las llamadas est ́andar de Berkeley Sockets (socket(), bind(), listen(), accept(),
//
//connect(), read(), write(), close()) y describa brevemente el flujo de ejecuci ́on.


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Faltan parámetros: hostname port");
        }

        String hostname = "localHost";
        int port = 5000;
        DataInputStream fromServer;
        DataOutputStream toServer;
        try {
            Socket socket = new Socket(hostname, port);
            toServer = new DataOutputStream(socket.getOutputStream());
            fromServer = new DataInputStream(socket.getInputStream());

            // No me hago responsable de lo de abajo (Tripa)
            toServer.writeUTF("POP");
            toServer.writeUTF("ping");
            String respuesta = fromServer.readUTF();
            System.out.println(respuesta);

            socket.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}
