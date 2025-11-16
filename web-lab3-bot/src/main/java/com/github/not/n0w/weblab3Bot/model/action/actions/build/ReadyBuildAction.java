package com.github.not.n0w.weblab3Bot.model.action.actions.build;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.action.actions.Action;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import com.github.not.n0w.weblab3Bot.service.imageGenerator.ImageGenerator;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReadyBuildAction implements Action {
    private final TemplateEngine templateEngine;
    private final KeyboardFactory keyboardFactory;


    private String generateReadyMessage(ChatState chatState) {
        Context context = new Context();
        context.setVariable("X", chatState.getX().entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
        context.setVariable("Y", chatState.getY());
        context.setVariable("R", chatState.getR());

        return templateEngine.process("ready", context);
    }

    @Override
    public ChatAnswer execute(String message, ChatState chatState) {
        ImageGenerator imageGenerator = new ImageGenerator();
        File image = imageGenerator.generateAimImageFile(
                chatState.getX().entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .map(Map.Entry::getKey)
                        .map(Double::valueOf)
                        .collect(Collectors.toList()),
                Double.valueOf(chatState.getY()),
                Double.valueOf(chatState.getR())
        );

        chatState.setState(State.READY);
        return ChatAnswer.builder()
                .chatState(chatState)
                .image(image)
                .messageText(generateReadyMessage(chatState))
                .answerChatKeyboard(keyboardFactory.getReplyKeyboardMarkup(chatState.getState()))
                .build();
    }
}
