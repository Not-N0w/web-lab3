package com.github.not.n0w.weblab3Bot.model;

import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.File;

@Data
@Builder
public class ChatAnswer {
    private String messageText;
    private String title;
    private File image;
    private ChatState chatState;
    private InlineKeyboardMarkup answerMessageKeyboard;
    private ReplyKeyboardMarkup answerChatKeyboard;
}
