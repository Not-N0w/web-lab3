package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ToggleXAction implements Action {
    private final KeyboardFactory keyboardFactory;

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        chatState.setX(
                chatState.getX().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getKey().equals(message) != e.getValue()
                        ))
        );
        return ChatAnswer.builder()
                .chatState(chatState)
                .answerMessageKeyboard(keyboardFactory.generateXButtons(chatState.getX()))
                .build();
    }
}