package pdytr.chatgrupal;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class Client {

    private int idUsuario;
    private String nombreUsuario;
    private ChatGrupalGrpc.ChatGrupalStub stub;
    private ManagedChannel channel;
    private StreamObserver<ChatGrupalProto.EnviarMensajeRequest> requestObserver;

    public Client(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        this.stub = ChatGrupalGrpc.newStub(channel);
        this.requestObserver = stub.chat(new StreamObserver<ChatGrupalProto.RecibirMensajeResponse>() {
        // Este "new StreamObserver" lo implementa el cliente: es para RECIBIR mensajes del servidor
        // El "requestObserver" que devuelve stub.chat(...) es para ENVIAR mensajes al servidor

            @Override
            public void onNext(ChatGrupalProto.RecibirMensajeResponse response) {
               System.out.println("[" + response.getMensaje().getTimestamp() + "] " + response.getMensaje().getUsuario() + ": " + response.getMensaje().getTexto());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("🎯 Chat finalizado");
            }
        });
    }

    public void conectar() {
        ChatGrupalProto.ConectarRequest request = ChatGrupalProto.ConectarRequest.newBuilder()
                .setUsuario(nombreUsuario)
                .build();

        stub.conectar(request, new StreamObserver<ChatGrupalProto.ConectarResponse>() {
            @Override
            public void onNext(ChatGrupalProto.ConectarResponse response) {
                idUsuario = response.getId();
                System.out.println("Conectado como " + nombreUsuario + " (ID: " + idUsuario + ")");
                System.out.println("Mensajes previos: \n" + response.getMensajesGuardadosList().toString());
                
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(" Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println(" Conexión finalizada");
            }
        });
    }

    public void chat(String mensajeDeTeclado) {

        ChatGrupalProto.EnviarMensajeRequest mensaje = ChatGrupalProto.EnviarMensajeRequest.newBuilder()
                .setMensaje(ChatGrupalProto.Mensaje.newBuilder()
                        .setUsuario(nombreUsuario)
                        .setTexto(mensajeDeTeclado)
                        .setTimestamp(java.time.LocalDateTime.now().toString())
                        .build())
                .build();
        requestObserver.onNext(mensaje);
        
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void cerrar() {
        ChatGrupalProto.DesconectarRequest request = ChatGrupalProto.DesconectarRequest.newBuilder()
                .setUsuario(nombreUsuario)
                .setId(idUsuario)
                .build();

        stub.desconectar(request, new StreamObserver<ChatGrupalProto.DesconectarResponse>() {
            @Override
            public void onNext(ChatGrupalProto.DesconectarResponse response) {
                System.out.println("✅ Desconectado: " + response.getMensaje());
                channel.shutdown();
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("🎯 Desconexión finalizada");
            }
        });
    }

    public void historialMensajes(){
        ChatGrupalProto.Empty request = ChatGrupalProto.Empty.newBuilder().build();
        stub.historialMensajes(request, new StreamObserver<ChatGrupalProto.HistorialMensajesResponse>() {
            @Override
            public void onNext(ChatGrupalProto.HistorialMensajesResponse response) {
                System.out.println("Historial de mensajes: \n" + response.getTextoList().toString());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Error al obtener historial: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("🎯 Historial recibido");
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        Client cliente = new Client("Usuario1");
        cliente.conectar();
        System.out.println("Conectandose al chat grupal...");
        boolean salir = false;
        while(!salir){
            String mensajeDeTeclado = System.console().readLine();
            if (mensajeDeTeclado.equals("/historial")){
                cliente.historialMensajes();
            }
            else if (mensajeDeTeclado.equals("/salir")){
                salir = true;
                System.out.println("Saliendo del chat...");

            }
            else{
                cliente.chat(mensajeDeTeclado);
            }
        }
        cliente.requestObserver.onCompleted();
        Thread.sleep(2000);
        cliente.cerrar();
    }
}
