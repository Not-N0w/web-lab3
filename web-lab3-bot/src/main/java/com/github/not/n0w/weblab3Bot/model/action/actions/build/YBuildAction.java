package com.github.not.n0w.weblab3Bot.model.action.actions.build;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.action.actions.Action;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class YBuildAction implements Action {
    private final KeyboardFactory keyboardFactory;

    private String generateChooseYMessage() {
        return "*Введите Y*";
    }

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        chatState.setState(State.ENTER_Y);
        return ChatAnswer.builder()
                .messageText("Отправьте Y текстом ниже")
                .chatState(chatState)
                .title(generateChooseYMessage())
                .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                .build();
    }
}
