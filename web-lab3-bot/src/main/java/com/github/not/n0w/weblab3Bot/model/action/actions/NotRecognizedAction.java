package com.github.not.n0w.weblab3Bot.model.action.actions;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
public class NotRecognizedAction implements Action {
    private final TemplateEngine templateEngine;

    private String generateCommandNotRecognizedMessage(String message) {
        Context context = new Context();
        context.setVariable("command", message);
        return templateEngine.process("not_recognized.txt", context);
    }

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        return ChatAnswer.builder()
                .messageText(generateCommandNotRecognizedMessage(message))
                .chatState(chatState)
                .build();
    }
}
