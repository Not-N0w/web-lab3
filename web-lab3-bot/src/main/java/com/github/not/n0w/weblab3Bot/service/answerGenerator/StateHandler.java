package com.github.not.n0w.weblab3Bot.service.answerGenerator;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.ChatMessage;
import com.github.not.n0w.weblab3Bot.model.action.ActionRegistry;
import com.github.not.n0w.weblab3Bot.model.action.actions.Action;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import lombok.Getter;
import org.thymeleaf.TemplateEngine;

@Getter
public class StateHandler {
    private final TemplateEngine templateEngine;
    private final State state;
    private final ActionRegistry actionRegistry;
    private final ActionRegistry callbackActionRegistry;

    public StateHandler(TemplateEngine templateEngine, ActionRegistry actionRegistry, ActionRegistry callbackActionRegistry, State state) {
        this.templateEngine = templateEngine;
        this.state = state;
        this.actionRegistry = actionRegistry;
        this.callbackActionRegistry = callbackActionRegistry;
    }


    public ChatAnswer handleState(String message, ChatState state) {
        Action action = actionRegistry.getAction(message);
        return action.execute(message, state);
    }
    public ChatAnswer handleCallback(String message, ChatState chatState) {
        Action action = callbackActionRegistry.getAction(message);
        return action.execute(message, chatState);
    }
}
