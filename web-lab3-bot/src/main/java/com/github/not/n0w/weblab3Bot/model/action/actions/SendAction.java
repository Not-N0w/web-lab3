package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.Hit;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.service.transmitterService.TransmitterService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SendAction implements Action {
    private final TransmitterService transmitterService;

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        List<Hit> hits = new ArrayList<>();
        for(var x : chatState.getX().entrySet()) {
            if(x.getValue()) {
                Hit hit = new Hit();
                hit.setX(new BigDecimal(x.getKey()));
                hit.setY(new BigDecimal(chatState.getY()));
                hit.setR(new BigDecimal(chatState.getR()));
                hits.add(hit);
            }
        }
        transmitterService.sendHits(hits, chatState.getSessionId());
        return ChatAnswer.builder().messageText("OK").build();
    }
}
