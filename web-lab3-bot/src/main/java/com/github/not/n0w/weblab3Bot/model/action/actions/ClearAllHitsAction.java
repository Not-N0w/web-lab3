package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.service.transmitterService.TransmitterService;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClearAllHitsAction implements Action {
    private final TransmitterService transmitterService;
    private final KeyboardFactory keyboardFactory;

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        transmitterService.clearAllHits(chatState.getSessionId());
            return ChatAnswer.builder()
                    .messageText("Очищено")
                    .chatState(chatState)
                    .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                    .build();
    }
}
