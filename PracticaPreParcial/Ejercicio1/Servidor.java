
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{
    
    public static void main(String[] args) {
        OutputStream toClient;
        InputStream fromClient;
        try{
            ServerSocket s = new ServerSocket(5000);
            while(true){
                Socket socketConectado = s.accept();
                byte[] recibi = new byte[50000];
                fromClient = socketConectado.getInputStream();
                recibi=fromClient.readAllBytes();
                System.out.print(new String(recibi));
                byte[] mando = "pong".getBytes();
                toClient = socketConectado.getOutputStream();
                toClient.write(mando);
            }
        }catch(Exception e){

        }
    }
}
