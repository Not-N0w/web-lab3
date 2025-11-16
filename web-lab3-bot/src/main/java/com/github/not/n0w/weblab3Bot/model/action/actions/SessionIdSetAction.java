package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.service.transmitterService.TransmitterService;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
public class SessionIdSetAction implements Action {
    private final KeyboardFactory keyboardFactory;
    private final TransmitterService transmitterService;
    private final TemplateEngine templateEngine;

    private String generateSessionSetText(String sessionId) {
        Context context = new Context();
        context.setVariable("sessionId", sessionId);
        return templateEngine.process("id_set", context);
    }

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        transmitterService.sync(chatState.getSessionId(), message);

        chatState.setSessionId(message);
        return ChatAnswer.builder()
                .messageText(generateSessionSetText(chatState.getSessionId()))
                .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                .chatState(chatState)
                .build();
    }
}
