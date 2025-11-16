package com.github.not.n0w.weblab3Bot.model.action;

import com.github.not.n0w.weblab3Bot.model.action.actions.Action;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ActionRegistry {
    private Map<String, Action> actions = new HashMap<>();

    @Setter
    private Action defaultAction;

    public void addAction(String name, Action action) {
        actions.putIfAbsent(name, action);
    }

    public Action getAction(String name) {
        return actions.getOrDefault(name, defaultAction);
    }
}
