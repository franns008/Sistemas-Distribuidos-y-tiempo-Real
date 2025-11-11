package EjercicioGPT8sock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Servidor {

    public static void main(String[] args) {
        DataOutputStream toClient;
        DataInputStream fromClient;
        try {
            ServerSocket s = new ServerSocket(7070);
            while (true) {
                Socket client = s.accept();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
                            DataInputStream fromClient = new DataInputStream(client.getInputStream());
                            while (client.isConnected()){
                                String candenaRecibida = fromClient.readUTF();
                                System.out.println("Recibi " + candenaRecibida);
                                toClient.writeUTF(candenaRecibida + client.getInetAddress());
                            }
                        } catch (Exception e) {
                            System.out.println("El cliente cerro la conexion");
                        }

                    }
                });
                t.start();
                t.join();
                System.out.println("Mate un hilo");
            }
        }catch (Exception e){
            System.out.println("Se produjo un error");
        }



    }

}
