import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Cliente {
    public static void main(String[] args) {
        String dirSever = "";
        int port=0;
        try{
            Socket s = new Socket(dirSever,port);
            OutputStream output = s.getOutputStream();
            String ping = "ping";
            output.write(ping.getBytes());
            InputStream input = s.getInputStream();
            byte[] respuesta=new byte[80000];
            input.read(respuesta);
            System.out.println("Respuesta" + new String(respuesta));
        }catch(Exception e){

        }
    

    }
}