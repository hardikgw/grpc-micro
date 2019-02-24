package biz.cits.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class PingServer {
    private static final Logger logger = Logger.getLogger(PingServer.class.getName());

    private Server server;

    private void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            PingServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final PingServer server = new PingServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class GreeterImpl extends PingServiceGrpc.PingServiceImplBase {
        @Override
        public void whatsUp(PingRequest req, StreamObserver<PingReply> responseObserver) {
            PingReply reply = PingReply.newBuilder().setStatus("Status Of Shout " + req.getShout()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}