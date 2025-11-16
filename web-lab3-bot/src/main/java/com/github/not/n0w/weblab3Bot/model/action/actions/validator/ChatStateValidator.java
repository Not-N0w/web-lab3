package com.github.not.n0w.weblab3Bot.model.action.actions.validator;

import com.github.not.n0w.weblab3Bot.model.state.ChatState;

public interface ChatStateValidator {
    ValidatorResponse validateChatState(ChatState chatState);
}
