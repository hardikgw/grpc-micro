package biz.cits.service.grpc;

import biz.cits.service.PingReply;
import biz.cits.service.PingRequest;
import biz.cits.service.PingServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class PingServer implements ApplicationRunner {
    private static final Logger logger = Logger.getLogger(PingServer.class.getName());

    private Server server;

    @Value("${grpc.server.port}")
    private Integer grpcServerPort;

    private void start() throws IOException {
        server = ServerBuilder.forPort(grpcServerPort)
                .addService(new GreeterImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + grpcServerPort);
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        start();
    }

    static class GreeterImpl extends PingServiceGrpc.PingServiceImplBase {
        @Override
        public void getStatus(PingRequest req, StreamObserver<PingReply> responseObserver) {
            PingReply reply = PingReply.newBuilder().setRespond("You said " + req.getShout()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}