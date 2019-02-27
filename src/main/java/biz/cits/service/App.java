package biz.cits.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;


@SpringBootApplication
public class App {

    public static void main(String[] args)  {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public PingServiceGrpc.PingServiceBlockingStub blockingStub() {
        return PingServiceGrpc.newBlockingStub(managedChannel());
    }

    private ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
    }


    @Bean
    public ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter() {
        return new ProtobufJsonFormatHttpMessageConverter();
    }

}
