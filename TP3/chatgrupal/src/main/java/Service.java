package pdytr.chatgrupal;

import io.grpc.stub.StreamObserver;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Service extends ChatGrupalGrpc.ChatGrupalImplBase {

    // Lista estática para guardar mensajes en memoria
    public static List<String> mensajes = new CopyOnWriteArrayList<>();
    public static List<String> usuarios = new CopyOnWriteArrayList<>();

    @Override
    public void conectar(ChatGrupalProto.ConectarRequest request,
                         StreamObserver<ChatGrupalProto.ConectarResponse> responseObserver) {

        String usuario = request.getUsuario();
        usuarios.add(usuario);
        String mensaje = "Usuario " + usuario + " conectado al chat grupal.";

        ChatGrupalProto.ConectarResponse response = ChatGrupalProto.ConectarResponse.newBuilder()
                .addMensajesGuardados(mensajes.toString());
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("[SERVER] " + mensaje);
    }

    @Override
    public void enviarMensaje(ChatGrupalProto.EnviarMensajeRequest request,
                              StreamObserver<ChatGrupalProto.EnviarMensajeResponse> responseObserver) {

        ChatGrupalProto.Mensaje msg = request.getMensaje();
        String usuario = msg.getUsuario();
        String texto = msg.getTexto();

        String registro = LocalDateTime.now() + " - " + usuario + ": " + texto;
        mensajes.add(registro);

        ChatGrupalProto.EnviarMensajeResponse response = ChatGrupalProto.EnviarMensajeResponse.newBuilder()
                .setMensaje("Mensaje recibido de " + usuario)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        System.out.println("[SERVER] " + registro);
    }
}
