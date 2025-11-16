package com.github.not.n0w.weblab3Bot.service.answerGenerator;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.ChatMessage;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnswerGeneratorService {

    private final Map<State, StateHandler> handlers;

    public AnswerGeneratorService(List<StateHandler> handlerList) {
        handlers = handlerList.stream()
                .collect(Collectors.toMap(StateHandler::getState, h -> h));
    }

    public ChatAnswer generateAnswer(String message, ChatState state) {
        StateHandler handler = handlers.get(state.getState());
        if (handler == null) return null;

        return handler.handleState(message, state);
    }

    public ChatAnswer generateCallbackAnswer(String message, ChatState chatState) {
        StateHandler handler = handlers.get(chatState.getState());
        if (handler == null) return null;

        return handler.handleCallback(message, chatState);
    }

}
