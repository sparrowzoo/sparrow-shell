package com.sparrow.controller;

import com.sparrow.context.SessionContext;
import com.sparrow.protocol.ClientInformation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientInfoController {
    @RequestMapping("api/auto-client-info")
    public ClientInformation clientInfo(ClientInformation clientInformation) {
        return clientInformation;
    }

    @RequestMapping("api/client-info-by-context")
    public ClientInformation clientInfo() {
        return SessionContext.getClientInfo();
    }
}
