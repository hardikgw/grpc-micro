package biz.cits.service.web;

import biz.cits.service.PingReply;
import biz.cits.service.PingRequest;
import biz.cits.service.PingServiceGrpc;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller()
public class PingClient {

    @Autowired
    PingServiceGrpc.PingServiceBlockingStub blockingStub;

    @Autowired
    ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter;

    @Value("${json.client.port}")
    private Integer jsonClientPort;

    @Value(("${json.client.host}"))
    private String jsonClientHost;


    @GetMapping("/grpc")
    @ResponseBody
    public String gRpcPing() throws InvalidProtocolBufferException {
        PingRequest request = PingRequest.newBuilder().setShout("Hi").build();
        PingReply resp = blockingStub.getStatus(request);
        return JsonFormat.printer().print(resp);
    }

    @GetMapping("/json")
    @ResponseBody
    public String gRestPing() {
        return "ok";
    }

    @GetMapping("/status")
    @ResponseBody
    public String getStatus() {
        String hostname = System.getenv().getOrDefault("HOSTNAME", "Unknown");
        return "ok " + hostname;
    }

    @GetMapping("/client")
    @ResponseBody
    public String getClientStatus() {
        RestTemplate restTemplate = new RestTemplate();
        String client_resp = restTemplate.getForObject("http://" + jsonClientHost + ':' + jsonClientPort + "/json", String.class);
        return client_resp;
    }

}
