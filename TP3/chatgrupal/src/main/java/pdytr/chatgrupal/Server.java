package pdytr.chatgrupal;

public class Server {

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
