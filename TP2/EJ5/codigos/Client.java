import java.io.*;
import java.net.*;

public class Client
{
  public static void main(String[] args) throws IOException
  {
    if ((args.length != 3) || (Integer.valueOf(args[1]) <= 0) )
    {
      System.out.println("2 arguments needed: serverhostname port");
      System.exit(1);
    }

    int DELAY_ON = Integer.parseInt(args[2]);

    /* The socket to connect to the echo server */
    Socket socketwithserver = null;
    
    if (DELAY_ON == 1){
        try {
		System.out.println("Me duermo 10 segundos, soy el cliente");
        	Thread.sleep(10 * 1000);
        } catch (InterruptedException ie) {
		Thread.currentThread().interrupt();
		return;
	}
    }
    SocketAddress remote = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
    // Reintentar conexión hasta que el servidor esté disponible (sin timeout explícito)
    while (true) {
      try {
        socketwithserver = new Socket();
        socketwithserver.connect(remote);
        break; // conectado
      } catch (IOException e) {
        try {
          Thread.sleep(500); // evitar busy-wait
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
          System.out.println("Interrumpido mientras esperaba al servidor");
          return;
        }
      }
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

      /* Send read data to server */
      long t1 = System.nanoTime();
      toserver.writeInt(inputline.length());
      toserver.write(buffer, 0, buffer.length);
      
      /* Recv data back from server (get space) */
      // Reservo buffer
      buffer = new byte[size];

      int leidosDeVuelta;
      
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

      if (buffer[buffer.length - 1] == 'b' && str.length() == size) {
        System.out.println("El mensaje llego correctamente");
      }else{
        System.out.println("El mensaje NO llego correctamente");
      }
      
      long total = (t4 - t1) / 1000000;   // tiempo de recepción en nanosegundos

      long tiempoPromedio = (total) / 2;
      System.out.println("El tiempo elapsed (ej5) fue de "+ tiempoPromedio + "ms");

    }
    System.out.println("El test del ejercicio 5 ha finalizado correctamente");
    fromserver.close();
    toserver.close();
    socketwithserver.close();
    
  }
  
}
