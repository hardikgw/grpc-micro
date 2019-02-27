package biz.cits.service.web;

import biz.cits.service.PingRequest;
import biz.cits.service.PingServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PIngClient {

    @Autowired
    PingServiceGrpc.PingServiceBlockingStub blockingStub;


    @GetMapping("/ping")
    @ResponseBody
    public String gRpcPing() {
        PingRequest request = PingRequest.newBuilder().setShout("Hi").build();
        return blockingStub.whatsUp(request).toString();
    }

}
