package biz.cits.service.web;

import biz.cits.service.PingReply;
import biz.cits.service.PingRequest;
import biz.cits.service.PingServiceGrpc;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import java.util.Arrays;
import java.util.List;

@Controller()
public class PingClient {

    @Autowired
    PingServiceGrpc.PingServiceBlockingStub blockingStub;

    @Autowired
    ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter;

    @Value("${json.client.port}")
    private Integer jsonClientPort;

    @Value("${json.client.host}")
    private String jsonClientHost;

    @Value("${json.client.endpoint}")
    private String jsonClientEndpoint;

    @Value("${status.latency.seconds}")
    private long statusLatencySeconds;

    @Value("${client.latency.seconds}")
    private long clientLatencySeconds;

    @GetMapping("/grpc")
    @ResponseBody
    public String gRpcPing() throws InvalidProtocolBufferException {
        PingRequest request = PingRequest.newBuilder().setShout("Hi").build();
        PingReply resp = blockingStub.getStatus(request);
        return JsonFormat.printer().print(resp);
    }

    @GetMapping("/json")
    public ResponseEntity<String> gRestPing(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok()
//                .headers(tracingHeaders(headers))
                .body("ok");
    }

    @GetMapping("/status")
    @ResponseBody
    public String getStatus() {
        String hostname = System.getenv().getOrDefault("HOSTNAME", "Unknown");
        try {
            TimeUnit.SECONDS.sleep(statusLatencySeconds);
        } catch (InterruptedException e) {

        }
        return "ok " + hostname;
    }

    @GetMapping("/client")
    @ResponseBody
    public String getClientStatus(@RequestHeader HttpHeaders headers) {
        try {
            TimeUnit.SECONDS.sleep(clientLatencySeconds);
        } catch (InterruptedException e) {
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .exchange("http://" + jsonClientHost + ':' + jsonClientPort + "/" + jsonClientEndpoint, HttpMethod.GET, new HttpEntity<>(tracingHeaders(headers)), String.class);
        return response.getBody();
    }

    private static HttpHeaders tracingHeaders(HttpHeaders headers) {
        HttpHeaders tracingHeaders = new HttpHeaders();
        extractHeader(headers, tracingHeaders, "x-request-id");
        extractHeader(headers, tracingHeaders, "x-b3-traceid");
        extractHeader(headers, tracingHeaders, "x-b3-spanid");
        extractHeader(headers, tracingHeaders, "x-b3-parentspanid");
        extractHeader(headers, tracingHeaders, "x-b3-sampled");
        extractHeader(headers, tracingHeaders, "x-b3-flags");
        extractHeader(headers, tracingHeaders, "x-ot-span-context");
        return tracingHeaders;
    }

    private static void extractHeader(HttpHeaders headers, HttpHeaders extracted, String key) {
        List<String> vals = headers.get(key);
        if (vals != null && !vals.isEmpty()) {
            extracted.put(key, Arrays.asList(vals.get(0)));
        }
    }

}
