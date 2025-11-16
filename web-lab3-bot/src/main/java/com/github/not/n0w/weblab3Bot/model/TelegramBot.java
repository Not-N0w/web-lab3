package com.github.not.n0w.weblab3Bot.model;

import com.github.not.n0w.weblab3Bot.service.TelegramBotService;
import com.github.not.n0w.weblab3Bot.utils.EscapeMarkdown;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    private TelegramBotService telegramBotService;
    public TelegramBot(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    public void outAnswer(ChatAnswer answer) {
        try {
            boolean titleExists = answer.getTitle() != null;
            String chatId = answer.getChatState().getChatId().toString();

            if (titleExists) {
                sendLongMessage(chatId, answer.getTitle(),
                        getOrRemove(answer.getAnswerChatKeyboard()));
            }

            String caption = answer.getMessageText() != null ? answer.getMessageText() : "";

            if (answer.getImage() != null) {
                execute(SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(answer.getImage()))
                        .caption(EscapeMarkdown.escapeMarkdown(caption.length() > 1024 ? caption.substring(0, 1024) : caption))
                        .replyMarkup(titleExists ? answer.getAnswerMessageKeyboard() : getOrRemove(answer.getAnswerChatKeyboard()))
                        .parseMode("MarkdownV2")
                        .build());
            }

            if (caption.length() > 1024) {
                sendLongMessage(chatId, caption, titleExists ? answer.getAnswerMessageKeyboard() : getOrRemove(answer.getAnswerChatKeyboard()));
            } else if (answer.getImage() == null && caption.length() > 0) {
                sendLongMessage(chatId, caption, titleExists ? answer.getAnswerMessageKeyboard() : getOrRemove(answer.getAnswerChatKeyboard()));
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendLongMessage(String chatId, String text, ReplyKeyboard keyboard) throws TelegramApiException {
        List<String> parts = splitByLength(text, 4096);
        for (int i = 0; i < parts.size(); i++) {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(EscapeMarkdown.escapeMarkdown(parts.get(i)))
                    .replyMarkup(i == parts.size() - 1 ? keyboard : null)
                    .parseMode("MarkdownV2")
                    .build());
        }
    }

    private List<String> splitByLength(String text, int maxLength) {
        List<String> parts = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + maxLength, text.length());
            parts.add(text.substring(start, end));
            start = end;
        }
        return parts;
    }

    private ReplyKeyboard getOrRemove(ReplyKeyboard keyboard) {
        return keyboard == null ? new ReplyKeyboardRemove(true) : keyboard;
    }



    @Override
    public void onUpdateReceived(Update update) {
        ChatAnswer chatAnswer;
        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            String data = update.getCallbackQuery().getData();

            chatAnswer = telegramBotService.onCallbackReceived(chatId, data);
            if(chatAnswer.getAnswerMessageKeyboard() != null) {
                EditMessageReplyMarkup editMarkup = EditMessageReplyMarkup.builder()
                        .chatId(String.valueOf(chatId))
                        .messageId(messageId)
                        .replyMarkup(chatAnswer.getAnswerMessageKeyboard())
                        .build();
                try {
                    execute(editMarkup);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            outAnswer(chatAnswer);
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();


            chatAnswer = telegramBotService.onMessageReceived(
                    new ChatMessage(messageText, chatId)
            );
            outAnswer(chatAnswer);
        }
    }


    @Override
    public String getBotUsername() {
        return "HitBot";
    }

    @Override
    public String getBotToken() {
        return "your_token";
    }
}
