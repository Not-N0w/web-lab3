package com.github.not.n0w.weblab3;

import com.github.not.n0w.weblab3.service.HitService;
import com.github.not.n0w.weblab3.service.TransmitterService;

import javax.enterprise.inject.spi.CDI;
import javax.naming.InitialContext;


import javax.faces.bean.ManagedProperty;
import javax.naming.NamingException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws")
public class WsEndpoint {


    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client connected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, NamingException {
        System.out.println("Received: " + message);
        TransmitterService transmitterService = CDI.current().select(TransmitterService.class).get();

        transmitterService.handleDataFromBot(message, session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Connection closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

}

