/*
 * Client.java
 * Just sends stdin read data to and receives back some data from the server
 *
 * usage:
 * java Client serverhostname port
 */

import java.io.*;
import java.net.*;

public class ClienteEj4
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

    try /* Connection with the server */
    { 
      socketwithserver = new Socket(args[0], Integer.valueOf(args[1]));
    }
    catch (Exception e)
    {
      System.out.println("ERROR connecting");
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
    
    /* Get some input from user 
    BufferedReader console  = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Please enter the message: ");
    String inputline = console.readLine();
    */

    
    for (int i = 1; i <= 6; i++) {
      int size = (int) Math.pow(10, i);

      String inputline = "a".repeat(size);
      /* Get the bytes... */
      buffer = inputline.getBytes();

      /* Send read data to server */
      long t1 = System.nanoTime();
      toserver.writeInt(inputline.length());
      toserver.write(buffer, 0, buffer.length);
      long t2 = System.nanoTime();

      System.out.println("Mensaje enviado");

      /* Recv data back from server (get space) */
      buffer = new byte[256];
      long t3 = System.nanoTime();
      fromserver.read(buffer);
      long t4 = System.nanoTime();
      /* Show data received from server */

      System.out.println("Tiempo de envio " +(t2 - t1)/1000000 +"ms");
      System.out.println("Tiempo de recepcion " + (t4 - t3)/1000000 +"ms");

      String resp = new String(buffer);
      System.out.println(resp);

    }

    fromserver.close();
    toserver.close();
    socketwithserver.close();
    
  }
  
}
