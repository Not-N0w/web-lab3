package com.github.not.n0w.weblab3Bot.model.action.actions.validator;

import com.github.not.n0w.weblab3Bot.model.state.ChatState;

import java.math.BigDecimal;

public class YValidator implements ChatStateValidator {
    @Override
    public ValidatorResponse validateChatState(ChatState chatState) {
        if(chatState.getY() == null){
            return new ValidatorResponse(false, "Y не задан !");
        }
        try {
            BigDecimal y = new BigDecimal(chatState.getY());
            return new ValidatorResponse(
                    y.compareTo(new BigDecimal(-5)) >= 0 && y.compareTo(new BigDecimal(3)) <= 0,
                    "Y должен быть в диапазоне от \\-5 до 3!"
            );
        }
        catch (Exception e) {
            return new ValidatorResponse(
                     false,
                    "Неверный формат Y!"
            );        }
    }
}
