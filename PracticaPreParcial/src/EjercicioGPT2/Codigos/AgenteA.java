package EjercicioGPT2.Codigos;
import jade.core.Agent;
import jade.core.ContainerID;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgenteA extends Agent {
    List<String> informacionDeLosCont;
    List<String> nombreContenedores;
    String origen;
    int index = 0;
    @Override
    protected void setup() {
        //List<String> nombreContenedor = (List<String>) getArguments()[0];
        nombreContenedores = List.of("Container-1","Container-2","Container-3");
        this.informacionDeLosCont = new ArrayList<>();
        this.origen = here().getName();
        System.out.println("Me muevo al contenedor "+nombreContenedores.get(index));
        doMove(new ContainerID(nombreContenedores.get(index),null));
        System.out.println("Termine de recorrer, me vuelvo a donde empece, "+origen);

    }

    @Override
    protected void afterMove() {
        if(!here().getName().equals(origen)){
            System.out.println("Estoy en el contenedor "+here().getName());
            this.informacionDeLosCont.add("Nombre del host: "+ManagementFactory.getOperatingSystemMXBean().getName()+"\n"+
                    "Carga promedio: "+ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
            this.index+=1;
            if (index < this.nombreContenedores.size()){
                doMove(new ContainerID(this.nombreContenedores.get(index),null));
            }else{
                doMove(new ContainerID(origen,null));
            }
        }else{
            System.out.println("Informacion de los contenedores:");
            for (String s : informacionDeLosCont){
                System.out.println(s);
            }
        }
    }
}
