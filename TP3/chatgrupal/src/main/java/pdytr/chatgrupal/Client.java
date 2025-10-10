package pdytr.chatgrupal;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class Client {

    private int idUsuario;
    private String nombreUsuario;
    private ChatGrupalGrpc.ChatGrupalStub stub;
    private ManagedChannel channel;

    public Client(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        this.stub = ChatGrupalGrpc.newStub(channel);
    }

    public void conectar() {
        ChatGrupalProto.ConectarRequest request = ChatGrupalProto.ConectarRequest.newBuilder()
                .setUsuario(nombreUsuario)
                .build();

        stub.conectar(request, new StreamObserver<ChatGrupalProto.ConectarResponse>() {
            @Override
            public void onNext(ChatGrupalProto.ConectarResponse response) {
                idUsuario = response.getId();
                System.out.println("✅ Conectado como " + nombreUsuario + " (ID: " + idUsuario + ")");
                System.out.println("Mensajes previos: " + response.getMensajesGuardadosList());
                
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("🎯 Conexión finalizada");
            }
        });
    }

    public void chat() {
        // Implementar la lógica de chat aquí
        StreamObserver<ChatGrupalProto.EnviarMensajeRequest> requestObserver = stub.chat(new StreamObserver<ChatGrupalProto.RecibirMensajeResponse>() { 
        // 📨 Este "new StreamObserver" lo implementa el cliente: es para RECIBIR mensajes del servidor
        // 💬 El "requestObserver" que devuelve stub.chat(...) es para ENVIAR mensajes al servidor

            @Override
            public void onNext(ChatGrupalProto.RecibirMensajeResponse response) {
                System.out.println("💬 " + response.getMensaje());
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

        // Enviar mensajes de ejemplo
        for (int i = 1; i <= 5; i++) {
            ChatGrupalProto.EnviarMensajeRequest mensaje = ChatGrupalProto.EnviarMensajeRequest.newBuilder()
                    .setMensaje(ChatGrupalProto.Mensaje.newBuilder()
                            .setUsuario(nombreUsuario)
                            .setTexto("Hola " + i)
                            .build())
                    .build();
            requestObserver.onNext(mensaje);
        }

        requestObserver.onCompleted();
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

    public static void main(String[] args) throws InterruptedException {
        Client cliente = new Client("Usuario1");
        cliente.conectar();

        Thread.sleep(2000);
        System.out.println("🆔 ID guardado en cliente: " + cliente.getIdUsuario());
        cliente.cerrar();
    }
}
