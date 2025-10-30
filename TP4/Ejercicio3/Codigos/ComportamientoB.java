import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.*;

public class ComportamientoB extends OneShotBehaviour    {

    private String idOrigen;
    private List<String> idsDestino;

    public ComportamientoB(Agent a, List<String> idsDestino, String idOrigen) {
        super(a);
        this.idsDestino = idsDestino;
        this.idOrigen = idOrigen;
    }

    @Override
    public void action() {
        List<String> idDestino = (List<String>) myAgent.getArguments()[0];

        for (String id : idDestino) {
            myAgent.doMove(new ContainerID(id, null));
            
        }
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("AgenteA", AID.ISLOCALNAME));
        msg.setContent("[AgenteB] Ya termine mi tarea");
        myAgent.send(msg);
    }

}