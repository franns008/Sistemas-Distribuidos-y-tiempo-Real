package EjercicioParcial.Codigos;

/*
Programe un agente movil en Java denominado SumadorAgente que calcule la suma de
todos los n ́umeros almacenados en un archivo de texto ubicado en una computadora
remota.

El agente debe:
1. Migrar desde el contenedor de origen al contenedor remoto indicado.
2. Leer el archivo localmente en el host remoto.
3. Volver al contenedor de origen y mostrar la suma total.


Condiciones: Java 17 y JADE 4.5 ya importados. Plataformas conectadas: Main-Container
(host A) y Remoto-1 (host B). Archivo remoto: /home/alumno/data/nums.txt. El agente
se lanza con tres argumentos: hostDestino, containerDestino y rutaRelativa. Supon-
ga happy path (sin errores de red ni permisos).
 */

import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;

public class AgenteA extends Agent {


    String rutaRelativa;
    Location origen;
    int suma = 0;

    @Override
    protected void setup() {
        // String hostDestino = getArguments()[0];
        // String containerDestino = getArguments()[1];

        String hostDestino = "localHost";
        String containerDestino = "Container-1";
        this.rutaRelativa = "/home/alumno/data/nums.txt";
        this.origen = here();
        ContainerID idContenedor = new ContainerID();
        idContenedor.setName(containerDestino);
        idContenedor.setAddress(hostDestino);
        // El agente se mueve al containerDestino en localhost (por eso null)
        doMove(idContenedor);

        /*
         doMove(new ContainerID(containerDestino,null));
         */
    }

    @Override
    protected void afterMove() {
        if(!here().getName().equals(this.origen.getName())) {
            // Si el agente NO se encuentra en el origen
            try {
                FileReader f = new FileReader(this.rutaRelativa);
                BufferedReader bf = new BufferedReader(f);
                String linea;
                while((linea = bf.readLine()) != null){
                    try {
                        suma += Integer.parseInt(linea);
                    } catch (NumberFormatException e) {
                        System.out.println("Se leyó un string no numérico! Sigo con el siguiente.");
                    }
                }
                doMove(origen);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            // Si el agente SI se encuentra en el origen
            System.out.println("La suma de los números en la computadora remota dió: " + suma + "!");
        }

    }


}
