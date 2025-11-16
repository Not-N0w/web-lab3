package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChangeRAction implements Action {
    private final KeyboardFactory keyboardFactory;

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {

        chatState.setR(message);
        return ChatAnswer.builder()
                .chatState(chatState)
                .answerMessageKeyboard(keyboardFactory.createControlRButtons(
                        List.of("1.0", "1.5", "2.0", "2.5", "3.0"),
                        chatState.getR()
                ))
                .build();
    }
}
