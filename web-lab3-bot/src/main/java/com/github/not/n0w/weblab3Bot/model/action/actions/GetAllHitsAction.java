package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.service.transmitterService.TransmitterService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllHitsAction implements Action {
    private final TransmitterService transmitterService;


    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        transmitterService.requestAllHits(chatState.getSessionId());
            return ChatAnswer.builder()
                    .messageText("Запрос на получение попаданий отправлен")
                    .chatState(chatState)
                    .build();
    }
}
