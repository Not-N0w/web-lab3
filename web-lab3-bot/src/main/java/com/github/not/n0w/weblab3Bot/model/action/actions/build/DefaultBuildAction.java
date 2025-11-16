package com.github.not.n0w.weblab3Bot.model.action.actions.build;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.action.actions.Action;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.AllArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@AllArgsConstructor
public class DefaultBuildAction implements Action {
    private final TemplateEngine templateEngine;
    private final KeyboardFactory keyboardFactory;


    private String generateHelloMessage(String chatId) {
        Context context = new Context();
        context.setVariable("chatId", chatId);
        return templateEngine.process("default", context);
    }

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        chatState.setState(State.DEFAULT);
        return ChatAnswer.builder()
                .messageText(generateHelloMessage(chatState.getSessionId()))
                .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                .chatState(chatState)
                .build();
    }
}
