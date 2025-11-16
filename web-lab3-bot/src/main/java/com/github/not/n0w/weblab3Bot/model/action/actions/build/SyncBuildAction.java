package com.github.not.n0w.weblab3Bot.model.action.actions.build;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.action.actions.Action;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class SyncBuildAction implements Action {
    private final KeyboardFactory keyboardFactory;

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        chatState.setState(State.SYNC);
        return ChatAnswer.builder()
                .messageText("Введите код с веб-страницы")
                .answerChatKeyboard(keyboardFactory.createReplyKeyboardMarkup(List.of("Назад")))
                .chatState(chatState)
                .build();
    }
}
