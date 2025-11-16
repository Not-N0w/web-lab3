package com.github.not.n0w.weblab3Bot.utils;

import com.github.not.n0w.weblab3Bot.model.state.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

public class KeyboardFactory {

    public record CallbackButton(String text, String callbackData) {}

    public InlineKeyboardMarkup buildInlineKeyboard(List<List<CallbackButton>> rows) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for(List<CallbackButton> row : rows ) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            for (CallbackButton s : row) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(s.text);
                button.setCallbackData(s.callbackData);
                buttonRow.add(button);
            }
            buttons.add(buttonRow);
        }
        return new InlineKeyboardMarkup(buttons);
    }
    public InlineKeyboardMarkup generateXButtons(Map<String, Boolean> x) {
        List<String> sortedKeys = new ArrayList<>(x.keySet());
        Collections.sort(sortedKeys);

        List<KeyboardFactory.CallbackButton> buttons = sortedKeys.stream()
                .map(key -> new KeyboardFactory.CallbackButton(
                        (x.getOrDefault(key, false) ? "[+] " : "[-] ") + key,
                        key
                ))
                .toList();

        List<List<KeyboardFactory.CallbackButton>> rows = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i += 5) {
            rows.add(buttons.subList(i, Math.min(i + 5, buttons.size())));
        }

        return buildInlineKeyboard(rows);
    }


    public InlineKeyboardMarkup createControlRButtons(List<String> avaliableR, String radius) {
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (String r : avaliableR) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText( (r.equals(radius) ? "[+] " + r : "[-] " + r));
            button.setCallbackData(r);
            row.add(button);
        }


        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(List.of(row));
        return keyboard;
    }


    public ReplyKeyboardMarkup createReplyKeyboardMarkup(List<String> items) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (String item : items) {
            row.add(new KeyboardButton(item));
        }
        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public ReplyKeyboardMarkup getReplyKeyboardMarkup(State state) {
        return switch (state) {
            case DEFAULT -> createReplyKeyboardMarkup(List.of("Hit", "Show hits", "Clear hits", "Set session"));
            case ENTER_X, ENTER_Y, ENTER_R -> createReplyKeyboardMarkup(List.of("Назад", "Далее"));
            case READY -> createReplyKeyboardMarkup(List.of("Назад", "Да"));
            case SYNC -> createReplyKeyboardMarkup(List.of("Ок"));
            default -> null;
        };
    }

}
