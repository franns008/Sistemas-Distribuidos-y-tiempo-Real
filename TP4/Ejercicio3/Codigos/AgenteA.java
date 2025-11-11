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

import java.util.*;

public class AgenteA extends Agent {
    private List<String> contenedoresId;

  /*  private void obtenerContenedores() {
        Set<String> contenedores = new HashSet<>();
        try {
            AMSAgentDescription[] agentes = AMSService.search(
                this,
                new AMSAgentDescription(),
                new SearchConstraints()
            );

            for (AMSAgentDescription desc : agentes) {
                AID id = desc.getName();
                String[] direcciones = id.getAddressesArray();
                if (direcciones != null && direcciones.length > 0) {
                    String direccion = direcciones[0];
                    // Extraer parte del contenedor (por ejemplo después del último "/")
                    contenedores.add(direccion);
                }
            }
            this.contenedoresId = new ArrayList<>(contenedores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */




        // Executed once during agent creation
    protected void setup(){
        System.out.println("[AgenteA] Hola, soy el agente A, con nombre local " + getLocalName().toString());
        System.out.println("[AgenteA] y voy a migrar al agente B al contenedor indicado..."); 
        String idOrigen = here().getID();
        String idDestino = "Main-Container";
        
        this.contenedoresId = List.of("Main-Container","contenedorEjercicio1");
        long inicioComunicacion = System.currentTimeMillis();
        AgentController controllerB = null;
        try {
            System.out.println("[AgenteA] Creando el agente B en el contenedor: "+here().getID());
            controllerB = getContainerController().createNewAgent("AgenteB", AgenteB.class.getName(), new Object[]{this.contenedoresId});
            controllerB.start();
        } catch (StaleProxyException e) {
            System.out.println("[AgenteA] Error en la creación del agenteB");
            e.printStackTrace();
        }
        ACLMessage msg = blockingReceive();
        System.out.println("[AgenteA] Recibí este mensaje del Agente B \n"+msg);
        long finComunicacion = System.currentTimeMillis();
        long tiempoTotal = finComunicacion-inicioComunicacion;
        System.out.println("[AgenteA] Tiempo total "+tiempoTotal);
    }

}