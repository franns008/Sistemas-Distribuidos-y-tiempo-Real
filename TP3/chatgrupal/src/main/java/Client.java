package pdytr.chatgrupal;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {

    int id;
    public static void main(String[] args) {
        // 1️⃣ Crear canal
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        ChatGrupalGrpc.ChatGrupalAsyncStub stub = ChatGrupalGrpc.newAsyncStub(channel);


        ChatGrupalProto.ConectarRequest request = ChatGrupalProto.ConectarRequest.newBuilder()
                .setUsuario("Usuario1")
                .build();


        ChatGrupalProto.ConectarResponse response = stub.conectar(request);


        System.out.println(response.getMensaje());

        channel.shutdown();
    }
}
