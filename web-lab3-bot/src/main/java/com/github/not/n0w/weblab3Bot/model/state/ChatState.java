package com.github.not.n0w.weblab3Bot.model.state;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
public class ChatState {
    private State state;
    private Map<String, Boolean> x;
    private String y;
    private String r;
    private String sessionId;
    private Long chatId;

    public ChatState(State state) {
        toDefault();
        this.sessionId = UUID.randomUUID().toString();

    }

    public void toDefault() {
        this.state = State.DEFAULT;
        this.r = "1.0";
        this.x = IntStream.rangeClosed(-5, 5)
                .boxed()
                .collect(Collectors.toMap(String::valueOf, i -> false));
        this.y = "";
    }
}
