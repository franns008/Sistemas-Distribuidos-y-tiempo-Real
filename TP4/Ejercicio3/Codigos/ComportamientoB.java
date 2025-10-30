import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class ComportamientoB extends OneShotBehaviour    {

    private String idOrigen;
    private String idDestino;

    public ComportamientoB(Agent a, String idDestino, String idOrigen) {
        super(a);
        this.idDestino = idDestino;
        this.idOrigen = idOrigen;
    }

    @Override
    public void action() {
        List<String> idDestino = (List<String>) myAgent.getArguments()[0];

            for (String id : idDestino) {
                myAgent.doMove(new ContainerID(id, null));
                System.out.println("[AgenteB] Voy a obtener informacion del sistema en el contenedor: " + myAgent.here().getID());

            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            double systemLoad = osBean.getSystemLoadAverage();
            double totalMemory = osBean.getTotalPhysicalMemorySize();
            double freeMemory = osBean.getFreePhysicalMemorySize();

            myAgent.doMove(new ContainerID(this.idOrigen, null));
            System.out.println("[AgenteB] Regresé al contenedor de origen: " + myAgent.here().getID());
            System.out.println("[AgenteB] Carga del sistema: " + systemLoad
                + ", Memoria total: " + totalMemory
                + ", Memoria libre: " + freeMemory);
           
        }
         ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("AgenteA"));
        msg.setContent("[AgenteB] Ya termine mi tarea");
        myAgent.send(msg);
    }

}