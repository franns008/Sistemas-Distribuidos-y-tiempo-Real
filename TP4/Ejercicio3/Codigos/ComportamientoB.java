import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.*;

public class ComportamientoB extends Behaviour {

    private String idOrigen;
    private List<String> idsDestino;
    private boolean termine;
    private boolean arranque=false;
    private List<String> idDestino;


    public ComportamientoB(Agent a, List<String> idsDestino, String idOrigen) {
        super(a);
        this.idsDestino = idsDestino;
        this.idOrigen = idOrigen;
    }

    @Override
    public void action() {
        idDestino = (List<String>) myAgent.getArguments()[0];
        for (String id : idDestino) {
            myAgent.doMove(new ContainerID(id, null));
            block();
        }

        myAgent.doMove(new ContainerID(idOrigen,null));
        System.out.println("Volvi y le robe todo al boludo del otro container");
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("AgenteA", AID.ISLOCALNAME));
        msg.setContent("[AgenteB] Ya termine mi tarea");
        myAgent.send(msg);
        termine = true;
    }

    @Override
    public boolean done() {
        return termine;
    }
}