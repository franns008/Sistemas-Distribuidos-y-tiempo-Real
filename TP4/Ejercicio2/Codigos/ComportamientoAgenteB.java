import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import jade.core.behaviours.OneShotBehaviour;



public class ComportamientoAgenteB extends OneShotBehaviour    {

    private String idOrigen;
    private String idDestino;

    public ComportamientoAgenteB(Agent a, String idDestino, String idOrigen) {
        super(a);
        this.idDestino = idDestino;
        this.idOrigen = idOrigen;
    }

    @Override
    public void action() {
        String idDestino = myAgent.getArguments()[0].toString();
        
        myAgent.doMove(new ContainerID(idDestino, null));
        System.out.println("[AgentB] Esperando 10 segundos...");
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
        }
        System.out.println("[AgentB] Voy a volver a la ubicacion desde donde migré: " + this.idOrigen);
        myAgent.doMove(new ContainerID(this.idOrigen, null));
        myAgent.doDelete();
        
    }

}