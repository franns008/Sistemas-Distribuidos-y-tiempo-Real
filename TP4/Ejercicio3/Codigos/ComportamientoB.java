import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

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
        String idDestino = myAgent.getArguments()[0].toString();
        
        myAgent.doMove(new ContainerID(idDestino, null));
        System.out.println("[AgenteB] Voy a obtener informacion del sistema en el contenedor: " + myAgent.here().getID());    
        myAgent.doMove(new ContainerID(this.idOrigen, null));
        System.out.println("[AgenteB] Regresé al contenedor de origen: " + myAgent.here().getID());
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("AgenteA"));
        msg.setContent("[AgenteB] Ya termine mi tarea");
    }

}