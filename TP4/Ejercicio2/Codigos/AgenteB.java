import javax.xml.namespace.QName;

import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;

public class AgenteB extends Agent {

    private String idOrigen;
   
    

    @Override
    protected void setup(){
        String idDestino = getArguments()[0].toString();
        System.out.println("[AgentB] Hola, soy el agente B, con nombre local " + getLocalName());
        this.idOrigen = here().getID();
        System.out.println("[AgentB] Y nombre completo... " + getName());
        System.out.println("[AgentB] Y estoy en la ubicación " + this.idOrigen + "\n\n");
        System.out.println("[AgentB] Me voy a mover a la ubicación " + idDestino);
        addBehaviour(new ComportamientoAgenteB(this, idDestino, this.idOrigen));
    }
    
    @Override
    protected void afterMove() {
        Location origen = here();
        System.out.println("[AgentB] Hola, aparezco. Soy el agente B migrado, con nombre local " + getLocalName());
        System.out.println("[AgentB] Y nombre completo... " + getName());
        System.out.println("[AgentB] Y estoy en la ubicación " + origen.getID() + "\n\n");
        System.out.println("[AgentB] La migración ha sido exitosa.");
        System.out.println("[AgentB] Me duermo 5 segundos para que puedas ver que estoy aca...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}