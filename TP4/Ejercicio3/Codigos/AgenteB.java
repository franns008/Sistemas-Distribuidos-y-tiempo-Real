import java.util.Stack;

import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;





public class AgenteB extends Agent {

    // Executed once during agent creation
    protected void setup(){
        System.out.println("[AgenteB] Hola, soy el agente B, con nombre local " + getLocalName());
        System.out.println("[AgentB] Y nombre completo... " + getName());
        System.out.println("[AgentB] Y estoy en la ubicación " + here().getID() + "\n\n");
        String idOrigen = here().getID();
        String idDestino = getArguments()[0].toString();
        addBehaviour(new ComportamientoB(this, idDestino, idOrigen));
        
    }

    protected void afterMove() {
        Location origen = here();
        System.out.println("[AgenteB] Y estoy en la ubicación " + origen.getID() + "\n\n");
        System.out.println("[AgenteB] La migración ha sido exitosa.");
    }
}