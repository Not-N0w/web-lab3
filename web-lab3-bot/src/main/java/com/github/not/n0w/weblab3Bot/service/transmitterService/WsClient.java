package com.github.not.n0w.weblab3Bot.service.transmitterService;

import lombok.RequiredArgsConstructor;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
@RequiredArgsConstructor
public class WsClient {
    private final TransmitterService transmitterService;
    private Session session;

    public void connect(String uri) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(uri));
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        transmitterService.receive(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Disconnected: " + reason);
    }

    public void send(String msg) throws Exception {
        session.getBasicRemote().sendText(msg);
    }
}
