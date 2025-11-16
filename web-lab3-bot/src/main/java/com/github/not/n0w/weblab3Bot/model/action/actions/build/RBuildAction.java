package com.github.not.n0w.weblab3Bot.model.action.actions.build;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.action.actions.Action;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class RBuildAction implements Action {
    private final KeyboardFactory keyboardFactory;


    private String generateChooseRMessage() {
        return "*Введите R*";
    }

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        chatState.setState(State.ENTER_R);
        return ChatAnswer.builder()
                .answerMessageKeyboard(keyboardFactory.createControlRButtons(
                        List.of("1.0", "1.5", "2.0", "2.5", "3.0"),
                        chatState.getR()
                ))
                .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                .messageText("Нажмите на кнопки ниже, чтобы отметить нужный вариант")
                .chatState(chatState)
                .title(generateChooseRMessage())
                .build();
    }
}
