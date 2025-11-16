package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.action.actions.validator.ChatStateValidator;
import com.github.not.n0w.weblab3Bot.model.action.actions.validator.ValidatorResponse;
import com.github.not.n0w.weblab3Bot.model.action.actions.validator.YValidator;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class YSetAction implements Action {
    private final KeyboardFactory keyboardFactory;

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        message = message.replace(",", ".");

        ChatStateValidator validator = new YValidator();
        chatState.setState(State.ENTER_Y);
        chatState.setY(message);

        ValidatorResponse response = validator.validateChatState(chatState);
        return ChatAnswer.builder()
                .chatState(chatState)
                .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                .messageText(
                        response.isValid()
                                ? "Принято!"
                                : response.getErrorMessage()
                )
                .build();
    }

}
