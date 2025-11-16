package com.github.not.n0w.weblab3Bot.model.action.actions.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidatorResponse {
    private boolean valid;
    private String errorMessage;
}
