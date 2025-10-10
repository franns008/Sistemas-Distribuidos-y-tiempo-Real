package pdytr.chatgrupal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import io.grpc.stub.StreamObserver;

public class Service extends ChatGrupalGrpc.ChatGrupalImplBase {

    // Lista estática para guardar mensajes en memoria
    public static List<String> mensajes = new CopyOnWriteArrayList<>();
    public static List<String> usuarios = new CopyOnWriteArrayList<>();
    public static ConcurrentHashMap<Integer, StreamObserver<ChatGrupalProto.RecibirMensajeResponse>> observers =
        new ConcurrentHashMap<>();
    public static int idCounter = 0;

    @Override
    public StreamObserver<ChatGrupalProto.EnviarMensajeRequest> chat(
            StreamObserver<ChatGrupalProto.RecibirMensajeResponse> responseObserver) {
        // 📨 Este responseObserver lo usa el SERVIDOR para ENVIAR mensajes al cliente
        // 💬 El StreamObserver<EnviarMensajeRequest> que retorna el método lo usa el SERVIDOR para RECIBIR mensajes del cliente
        
        // Inicializo con un valor inválido
        return new StreamObserver<ChatGrupalProto.EnviarMensajeRequest>() {
            private int idAsocado = -1;
            @Override
            public void onNext(ChatGrupalProto.EnviarMensajeRequest request) {
                String mensaje = request.getMensaje().getUsuario() + " [" + LocalDateTime.now() + "]: "
                        + request.getMensaje().getTexto();
                
                // Aca agrego el observer del cliente que me envio el mensaje a la lista de observers y el id
                if (idAsocado == -1) {
                    idAsocado = request.getId();
                    observers.put(idAsocado, responseObserver);
                }
                System.out.println("[SERVER] Nuevo mensaje: " + mensaje);

                for (StreamObserver<ChatGrupalProto.RecibirMensajeResponse> observer : observers.values()) {
                    ChatGrupalProto.RecibirMensajeResponse response = ChatGrupalProto.RecibirMensajeResponse.newBuilder()
                            .setMensaje(mensaje)
                            .build();
                    observer.onNext(response);
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("❌ Error en el stream: " + t.getMessage());
                observers.remove(idAsocado);
            }

            @Override
            public void onCompleted() {
                System.out.println("🎯 Cliente terminó su stream");
                observers.remove(idAsocado);
                responseObserver.onCompleted();
            }
        };

    }


    @Override
    public void conectar(ChatGrupalProto.ConectarRequest request,
                         StreamObserver<ChatGrupalProto.ConectarResponse> responseObserver) {

        String usuario = request.getUsuario();
        usuarios.add(usuario);

        int id = idCounter;
        idCounter++;
        String mensaje = "Usuario " + usuario + " conectado al chat grupal.";

        ChatGrupalProto.ConectarResponse response = ChatGrupalProto.ConectarResponse.newBuilder()
                .addMensajesGuardados(mensajes.toString())
                .setId(id)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("[SERVER] " + mensaje);
    }

    @Override
    public void desconectar(ChatGrupalProto.DesconectarRequest request,
                            StreamObserver<ChatGrupalProto.DesconectarResponse> responseObserver) {

        String usuario = request.getUsuario();
        usuarios.remove(usuario);
        observers.remove(request.getId());
        String mensaje = "Usuario " + usuario + " desconectado del chat grupal.";

        ChatGrupalProto.DesconectarResponse response = ChatGrupalProto.DesconectarResponse.newBuilder()
                .setMensaje(mensaje)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("[SERVER] " + mensaje);
    }
   
}