import java.io.*;
import java.net.*;

public class Client
{
  public static void main(String[] args) throws IOException
  {
    /* Check the number of command line parameters */
    if ((args.length != 2) || (Integer.valueOf(args[1]) <= 0) )
    {
      System.out.println("2 arguments needed: serverhostname port");
      System.exit(1);
    }

    /* The socket to connect to the echo server */
    Socket socketwithserver = null;

    SocketAddress remote = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
    int CONNECT_TIMEOUT_MS = 10000; // Cuanto tiempo espero para conectarme 

    // CONSULTAR

    try {
        socketwithserver = new Socket();
        socketwithserver.connect(remote, CONNECT_TIMEOUT_MS);
    } catch (SocketTimeoutException e) {
        System.out.println("Timeout conectando al servidor tras " + CONNECT_TIMEOUT_MS + " ms");
        System.exit(1);
    } catch (IOException e) {
        System.out.println("ERROR conectando: " + e.getMessage());
        System.exit(1);
    } 

    /* Streams from/to server */
    DataInputStream  fromserver;
    DataOutputStream toserver;

    /* Streams for I/O through the connected socket */
    fromserver = new DataInputStream(socketwithserver.getInputStream());
    toserver   = new DataOutputStream(socketwithserver.getOutputStream());

    /* Buffer to use with communications (and its length) */
    byte[] buffer;
    
    String inputline;
    for (int i = 1; i <= 6; i++) {
      int size = (int) Math.pow(10, i);

      inputline = "a".repeat(size-1)+"b";
      /* Get the bytes... */
      buffer = inputline.getBytes();

      while (!socketwithserver.isConnected()) {
        // wait until the socket is connected
      }
      /* Send read data to server */
      long t1 = System.nanoTime();
      toserver.writeInt(inputline.length());
      toserver.write(buffer, 0, buffer.length);
      //long t2 = System.nanoTime();

      //System.out.println("Mensaje enviado de " + size + "bytes");
      // Aca debe haber un while
      /* Recv data back from server (get space) */
      // Reservo buffer
      buffer = new byte[size];

      int leidosDeVuelta;
      //long t3 = System.nanoTime();
      try {
        leidosDeVuelta = fromserver.read(buffer);
        if (leidosDeVuelta == -1) break;

      } catch (SocketException e){
        System.out.println("Servidor cerró la conexión inesperadamente.");
        break;
      }

      while (leidosDeVuelta < size){
        try {
                // ¿Sobre escribo? consultar
                leidosDeVuelta += fromserver.read(buffer, leidosDeVuelta, size - leidosDeVuelta);
            } catch (SocketException e) {
                System.out.println("Cliente cerró la conexión.");
                break;
            }
      }
      long t4 = System.nanoTime();
      
      /* Convert to string */
      String str = new String(buffer, 0, leidosDeVuelta);

      String strresp;
      if (buffer[buffer.length - 1] == 'b' && str.length() == size) {
        System.out.println("El mensaje llego correctamente");
        strresp ="El mensaje llego correctamente con longitud de:" + str.length();
      }else{
        System.out.println("El mensaje NO llego correctamente");
        strresp ="El mensaje NO llego correctamente";
      }
      
      long total = (t4 - t1) / 1000000;   // tiempo de recepción en nanosegundos

      long tiempoPromedio = (total) / 2;
      System.out.println("El tiempo elapsed fue de "+ tiempoPromedio + "ms");
      String resp = new String(buffer);
      

    }

    fromserver.close();
    toserver.close();
    socketwithserver.close();
    
  }
  
}
