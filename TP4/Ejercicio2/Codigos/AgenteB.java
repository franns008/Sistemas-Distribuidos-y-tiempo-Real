import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;

public class AgenteB extends Agent {

    private String idOrigen;
   
    // Executed once during agent creation
    protected void setup(){
        String idDestino = getArguments()[0].toString();
        System.out.println("[AgentB] Hola, soy el agente B, con nombre local " + getLocalName());
        this.idOrigen = here().getID();
        System.out.println("[AgentB] Y nombre completo... " + getName());
        System.out.println("[AgentB] Y estoy en la ubicación " + this.idOrigen + "\n\n");
        System.out.println("[AgentB] Me voy a mover a la ubicación " + idDestino);
        try {
            
            doMove(new ContainerID(idDestino, null));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[AgentB] Error al intentar mover el agente B.");
        }
    }

    protected void afterMove() {
        Location origen = here();
        System.out.println("[AgentB] Hola, aparezco. Soy el agente B migrado, con nombre local " + getLocalName());
        System.out.println("[AgentB] Y nombre completo... " + getName());
        System.out.println("[AgentB] Y estoy en la ubicación " + origen.getID() + "\n\n");
        System.out.println("[AgentB] La migración ha sido exitosa.");

        System.out.println("[AgentB] Voy a esperar 10 segundos antes de decirte de donde migré...");
        try {
            Thread.sleep(10000);
            System.out.println("[AgentB] Migré desde la ubicación " + this.idOrigen);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[AgentB] Eliminando agente B...");
        doDelete();
    }


}