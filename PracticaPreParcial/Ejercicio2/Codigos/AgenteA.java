import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import jade.wrapper.AgentController;

public class AgenteA extends Agent {

    // Executed once during agent creation
    protected void setup(){
        System.out.println("Agente levantado");
        
        //String direccionPorParametro = getParameters()[0];
        
        String direccion = "Container-2";
    }

    protected void afterMove(){
        
    }

}
