package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.action.actions.validator.ChatStateValidator;
import com.github.not.n0w.weblab3Bot.model.action.actions.validator.ValidatorResponse;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class TransferStateAction implements Action {
    private final Action buildState;
    private final KeyboardFactory keyboardFactory;
    private ChatStateValidator validator;


    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        if(validator == null) { return buildState.execute(message, chatState); }

        ValidatorResponse response = validator.validateChatState(chatState);
        if(response.isValid()) return buildState.execute(message, chatState);
        else return ChatAnswer.builder()
                .messageText(response.getErrorMessage())
                .chatState(chatState)
                .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                .build();
    }
}
