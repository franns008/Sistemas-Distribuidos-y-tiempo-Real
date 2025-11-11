package Ejercicio2.Codigos;
import jade.core.Agent;
import jade.core.ContainerID;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AgenteA extends Agent {
    private String destino;
    private String path;
    private String origen;
    long suma = 0;
    @Override
    protected void setup() {
        System.out.println("Hola soy el agente A");
       /* destino = getArguments()[0].toString();
        path = getArguments()[1].toString();
        */
        destino = "Container-2";
        path = "UnArchivo.txt";
        System.out.println("Me voy a mover al contenedor con nombre "+destino);
        this.origen = here().getName();
        doMove(new ContainerID(destino,null));

    }

    @Override
    protected void afterMove() {
        if(here().getName().equals(destino)){
            System.out.println("Estoy en el contenedor, voy a buscar el archivo "+path);
            try {
                FileReader file = new FileReader(this.path);
                BufferedReader bf = new BufferedReader(file);
                String line;
                while ((line = bf.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        suma += Long.parseLong(line);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            doMove(new ContainerID(this.origen,null));
        }else{
            System.out.println("Estoy en el main container, el resultado de la suma es "+suma);
        }
    }
}
