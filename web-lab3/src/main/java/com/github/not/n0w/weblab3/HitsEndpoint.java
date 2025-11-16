package com.github.not.n0w.weblab3;


import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/hits")
public class HitsEndpoint {
    private static Session session;

    @OnOpen
    public void onOpen(Session s) {
        session = s;
    }

    public static void send(String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
