
import java.util.List;


public class Server {
    
    public static List<String> mensajes = new java.util.ArrayList<>();
    public static List<String> usuariosConectados = new java.util.ArrayList<>();


    public static void main(String[] args) throws Exception {
        io.grpc.Server server = io.grpc.ServerBuilder.forPort(8080)
                .addService(new Service())
                .build();
        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started on port 8080");

        server.awaitTermination();
    }
}