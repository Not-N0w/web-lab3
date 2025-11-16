package com.github.not.n0w.weblab3Bot;

import com.github.not.n0w.weblab3Bot.model.TelegramBot;
import com.github.not.n0w.weblab3Bot.service.TelegramBotService;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class TelegramBotApplication {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotService telegramBotService = new TelegramBotService();
        telegramBotService.botStart();
    }
}
