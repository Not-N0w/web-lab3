package com.github.not.n0w.weblab3.service;

import com.github.not.n0w.weblab3.adapter.BigDecimalStringAdapter;
import com.github.not.n0w.weblab3.model.Hit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.inject.Named;
import javax.websocket.Session;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Named
@ApplicationScoped
public class TransmitterService {

    @Inject
    private HitService hitService;
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();


    public void handleDataFromBot(String message, Session session) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        Map<String, Object> data = gson.fromJson(message, Map.class);

        String msg = (String)data.get("message");
        if((String)data.get("sessionId") != null) {
            sessions.put((String) data.get("sessionId"), session);
        }
        if(msg.equals("clear")) {
            hitService.clearHits((String)data.get("sessionId"));
            return;
        }
        if(msg.equals("add")) {
            List<Map<String,Object>> rawHits = (List<Map<String,Object>>) data.get("hits");
            String finalSessionId = (String)data.get("sessionId");
            List<Hit> hits = rawHits.stream()
                    .map(m -> {
                        return hitService.hitSilent(
                                new BigDecimal(m.get("x").toString()),
                                new BigDecimal(m.get("y").toString()),
                                new BigDecimal(m.get("r").toString()),
                                finalSessionId
                        );
                    })
                    .toList();


            sendDataToBot(Map.of(
                    "sessionId", (String)data.get("sessionId"),
                    "hits", hits,
                    "message", "updated_hits"
            ), (String)data.get("sessionId"));
        }
        else if(msg.equals("get_all")) {
            sendDataToBot(Map.of(
                    "sessionId", (String)data.get("sessionId"),
                    "hits", hitService.getHits((String)data.get("sessionId")),
                    "message", "all"
            ), (String)data.get("sessionId"));
        }
        else if(msg.equals("clear_all")) {
            hitService.clearHits((String)data.get("sessionId"));
        }
        else if(msg.equals("sync")) {
            hitService.sync(
                    (String)data.get("oldSessionId"),
                    (String)data.get("newSessionId")
            );
            sessions.put((String)data.get("newSessionId"), sessions.get((String)data.get("oldSessionId")));
            sessions.remove((String)data.get("oldSessionId"));
            hitService.updateBySession((String)data.get("newSessionId"));
            return;
        }
        hitService.updateBySession((String)data.get("sessionId"));
    }

    public void sendDataToBot(Map<String, Object> wsData, String sessionId) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BigDecimal.class, new BigDecimalStringAdapter())
                .create();
        String json = gson.toJson(wsData);

        Session session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(json);
        }
    }

    public HitService getHitService() {
        return hitService;
    }

    public void setHitService(HitService hitService) {
        this.hitService = hitService;
    }
}
