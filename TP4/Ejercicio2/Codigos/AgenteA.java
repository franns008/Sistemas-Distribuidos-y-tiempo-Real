import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import jade.wrapper.AgentController;

public class AgenteA extends Agent {

    // Executed once during agent creation
    protected void setup(){
        System.out.println("\n\nHola, soy el agente A, con nombre local " + getLocalName());
        System.out.println("[AgentA] Y nombre completo... " + getName());
        System.out.println("[AgentA] Y estoy en la ubicación " + here().getID() + "\n\n");
        System.out.println("[AgentA] Intentando migrar al agente B al contenedor Main-Container");
        try{
            AgentController agenteB = getContainerController()
            .createNewAgent("AgenteB", "AgenteB", new Object[]{"Main-Container"});
            agenteB.start(); //aca le digo cuando empezar a ejecutarse
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[AgentA] Surgió un error al intentar crear el agente B: " + e.getMessage());
        }
        System.out.println("[AgentA] Agente B creado y en ejecución.");
        System.out.println("[AgentA] Termine mi mision, me voy a eliminar...");
        doDelete();
    }

}
