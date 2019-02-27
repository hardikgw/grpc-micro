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

@Controller
public class PIngClient {

    @Autowired
    PingServiceGrpc.PingServiceBlockingStub blockingStub;

    @Autowired
    ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter;


    @GetMapping("/ping")
    @ResponseBody
    public String gRpcPing() throws InvalidProtocolBufferException {
        PingRequest request = PingRequest.newBuilder().setShout("Hi").build();
        PingReply resp = blockingStub.whatsUp(request);
        return JsonFormat.printer().print(resp);
    }



}
