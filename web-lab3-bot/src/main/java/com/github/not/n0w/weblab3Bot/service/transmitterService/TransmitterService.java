package com.github.not.n0w.weblab3Bot.service.transmitterService;

import com.github.not.n0w.weblab3Bot.adapter.BigDecimalStringAdapter;
import com.github.not.n0w.weblab3Bot.model.Hit;
import com.github.not.n0w.weblab3Bot.service.TelegramBotService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TransmitterService {
    private String uri = "ws://localhost:18030/web-lab3-1.0-SNAPSHOT/ws";
    private WsClient wsClient = new WsClient(this);
    private final TelegramBotService telegramBotService;

    public TransmitterService(TelegramBotService telegramBotService) {
        try {
            wsClient.connect(uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.telegramBotService = telegramBotService;
    }

    public void sendHits(List<Hit> emptyHits, String sessionId) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BigDecimal.class, new BigDecimalStringAdapter())
                .create();
        Map<String, Object> toSend = Map.of(
                "message", "add",
                "hits", emptyHits,
                "sessionId", sessionId
        );
        String json = gson.toJson(toSend);
        try {
            wsClient.send(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void requestAllHits(String sessionId) {
        Gson gson = new Gson();
        Map<String, Object> toSend = Map.of(
                "message", "get_all",
                "sessionId", sessionId
        );
        String json = gson.toJson(toSend);
        try {
            wsClient.send(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAllHits(String sessionId) {
        Gson gson = new Gson();
        Map<String, Object> toSend = Map.of(
                "message", "clear_all",
                "sessionId", sessionId
        );
        String json = gson.toJson(toSend);
        try {
            wsClient.send(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void receive(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        Map<String, Object> data = gson.fromJson(json, Map.class);

        String message = (String) data.get("message");
        if(message.equals("update")) {
            String hitsJson = gson.toJson(data.get("hit"));

            Type type = new TypeToken<Hit>() {}.getType();
            Hit hit = gson.fromJson(hitsJson, type);

            telegramBotService.pushUpdatedHitsData(List.of(hit), hit.getSessionId(), true);
        }
        else if(message.equals("updated_hits")) {
            String hitsJson = gson.toJson(data.get("hits"));

            Type type = new TypeToken<List<Hit>>() {
            }.getType();
            List<Hit> hits = gson.fromJson(hitsJson, type);

            telegramBotService.pushUpdatedHitsData(hits, (String) data.get("sessionId"), true);
        }
        else if(message.equals("all")) {
            String hitsJson = gson.toJson(data.get("hits"));

            Type type = new TypeToken<List<Hit>>() {
            }.getType();
            List<Hit> hits = gson.fromJson(hitsJson, type);

            telegramBotService.pushUpdatedHitsData(hits, (String) data.get("sessionId"), false);
        }
    }

    public void sync(String oldSessionId, String newSessionId) {
        Gson gson = new Gson();
        Map<String, Object> toSend = Map.of(
                "message", "sync",
                "oldSessionId", oldSessionId,
                "newSessionId", newSessionId
        );
        System.out.println(gson.toJson(toSend));
        String json = gson.toJson(toSend);
        try {
            wsClient.send(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
