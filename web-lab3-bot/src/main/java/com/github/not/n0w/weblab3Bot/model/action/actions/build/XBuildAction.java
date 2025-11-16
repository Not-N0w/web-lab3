package com.github.not.n0w.weblab3Bot.model.action.actions.build;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.action.actions.Action;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class XBuildAction implements Action {
    private final KeyboardFactory keyboardFactory;

    private String generateChooseXMessage() {
        return "*Введите X*";
    }

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        chatState.setState(State.ENTER_X);
        return ChatAnswer.builder()
                .answerMessageKeyboard(keyboardFactory.generateXButtons(chatState.getX()))
                .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                .messageText("Нажмите на кнопки ниже, чтобы отметить нужные варианты")
                .chatState(chatState)
                .title(generateChooseXMessage())
                .build();
    }
}
