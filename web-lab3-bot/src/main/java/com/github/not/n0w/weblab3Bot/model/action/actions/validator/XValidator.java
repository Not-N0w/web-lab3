package com.github.not.n0w.weblab3Bot.model.action.actions.validator;

import com.github.not.n0w.weblab3Bot.model.state.ChatState;

public class XValidator implements ChatStateValidator {
    @Override
    public ValidatorResponse validateChatState(ChatState chatState) {
        return new ValidatorResponse(
                chatState.getX().values().stream().anyMatch(Boolean::booleanValue),
                "Должен быть выбран хотя бы один x!"
        );
    }
}
