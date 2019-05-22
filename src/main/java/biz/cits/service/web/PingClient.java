package biz.cits.service.web;

import biz.cits.service.PingReply;
import biz.cits.service.PingRequest;
import biz.cits.service.PingServiceGrpc;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/ping")
public class PingClient {

    @Autowired
    PingServiceGrpc.PingServiceBlockingStub blockingStub;

    @Autowired
    ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter;

    @GetMapping("/rpc")
    @ResponseBody
    public String gRpcPing() throws InvalidProtocolBufferException {
        PingRequest request = PingRequest.newBuilder().setShout("Hi").build();
        PingReply resp = blockingStub.getStatus(request);
        return JsonFormat.printer().print(resp);
    }

    @GetMapping("/json")
    @ResponseBody
    public String gRestPing() throws InvalidProtocolBufferException {
        return "ok";
    }

}
