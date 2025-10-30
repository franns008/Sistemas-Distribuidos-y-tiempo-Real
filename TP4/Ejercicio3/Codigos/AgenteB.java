import java.util.Stack;

import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.*;



public class AgenteB extends Agent {

    // Executed once during agent creation
    protected void setup(){
        System.out.println("[AgenteB] Hola, soy el agente B, con nombre local " + getLocalName());
        System.out.println("[AgentB] Y nombre completo... " + getName());
        System.out.println("[AgentB] Y estoy en la ubicación " + here().getID() + "\n\n");
        String idOrigen = here().getID();
        List<String> idsDestino = (List<String>) getArguments()[0];
        addBehaviour(new ComportamientoB(this, idsDestino, idOrigen));

    }

    protected void afterMove() {
        Location origen = here();
        System.out.println("[AgenteB] Voy a obtener informacion del sistema en el contenedor: " +here().getID());
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double systemLoad = osBean.getSystemLoadAverage();
        double totalMemory = osBean.getTotalPhysicalMemorySize();
        double freeMemory = osBean.getFreePhysicalMemorySize();

        System.out.println("[AgenteB] Información del sistema en el contenedor " + here().getID() + ":");
        System.out.println("[AgenteB] Carga del sistema: " + systemLoad);
        System.out.println("[AgenteB] Memoria total: " + totalMemory);
        System.out.println("[AgenteB] Memoria libre: " + freeMemory);
        System.out.println("[AgenteB] migracion exitosa \n\n"  );
    }
}