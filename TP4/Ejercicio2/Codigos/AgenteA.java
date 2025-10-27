import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;

public class AgenteA extends Agent {

    // Executed once during agent creation
    protected void setup(){
        System.out.println("\n\nHola, soy el agente A, con nombre local " + getLocalName());
        System.out.println("[AgentA] Y nombre completo... " + getName());
        System.out.println("[AgentA] Y estoy en la ubicación " + here().getID() + "\n\n");
        System.out.println("[AgentA] Intentando migrar al agente B al contenedor Main-Container");
        try{
            getContainerController().createNewAgent("AgenteB", "AgenteB", new Object[]{"Main-Container"}).start();
            System.out.println("[AgentA] Agente B creado y en ejecución.");
            System.out.println("[AgentA] Termine mi mision, me voy a eliminar...");
            doDelete();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("[AgentA] Surgió un error al intentar crear el agente B: " + e.getMessage());
        }
    }

}
