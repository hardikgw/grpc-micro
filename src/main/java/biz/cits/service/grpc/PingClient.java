package biz.cits.service.grpc;

import biz.cits.service.PingReply;
import biz.cits.service.PingRequest;
import biz.cits.service.PingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PingClient {
    private static final Logger logger = Logger.getLogger(PingClient.class.getName());

    private final ManagedChannel channel;
    private final PingServiceGrpc.PingServiceBlockingStub blockingStub;

    private PingClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build());
    }

    PingClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = PingServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        PingRequest request = PingRequest.newBuilder().setShout(name).build();
        PingReply response;
        try {
            response = blockingStub.whatsUp(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("PingRequest: " + response.getStatus());
    }

    public static void main(String[] args) throws Exception {
        PingClient client = new PingClient("localhost", 50051);
        try {
            String shout = "Ok";
            if (args.length > 0) {
                shout = args[0];
            }
            client.greet(shout);
        } finally {
            client.shutdown();
        }
    }
}