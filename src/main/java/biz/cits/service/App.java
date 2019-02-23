package biz.cits.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App {

    private Server server;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
