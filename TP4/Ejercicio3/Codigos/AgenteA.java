import jade.core.Agent;
import jade.core.Location;
import jade.core.ContainerID;
import jade.core.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

public class AgenteA extends Agent {
    private List<String> contenedoresId;

    private void obtenerContenedores(){
        this.contenedoresId = new ArrayList<>();
        try {
            // Buscar todos los agentes en la plataforma
            AMSAgentDescription[] agents = AMSService.search(
                    this,
                    new AMSAgentDescription(),
                    new SearchConstraints()
            );



            for (AMSAgentDescription desc : agents) {
                AID agentID = desc.getName();
                String addresses[] = agentID.getAddressesArray();
                if (addresses != null && addresses.length > 0) {
                    // La dirección contiene info del contenedor
                    contenedoresId.add(addresses[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



        // Executed once during agent creation
    protected void setup(){
        System.out.println("[AgenteA] Hola, soy el agente A, con nombre local " + getLocalName());
        System.out.println("[AgenteA] y voy a migrar al agente B al contenedor indicado..."); 
        String idOrigen = here().getID();
        String idDestino = "Main-Container";
        this.obtenerContenedores();
        long inicioComunicacion = System.currentTimeMillis();
        AgentController controllerB = null;
        for(String id : this.contenedoresId){
            try {

                controllerB = getContainerController().createNewAgent("AgenteB", AgenteB.class.getName(), new Object[]{id, idOrigen});
                controllerB.start();
            } catch (StaleProxyException e) {
                System.out.println("Error en la del agenteB");;
            }
            ACLMessage msg = blockingReceive();
            System.out.println("[AgenteA] Recibí este mensaje del Agente B \n"+msg);
        }
        long finComunicacion = System.currentTimeMillis();
        long tiempoTotal = finComunicacion-inicioComunicacion;
        System.out.println("[AgenteA] Tiempo total "+tiempoTotal);
    }

}