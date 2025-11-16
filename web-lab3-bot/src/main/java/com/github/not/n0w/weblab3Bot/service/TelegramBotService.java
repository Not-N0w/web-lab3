package com.github.not.n0w.weblab3Bot.service;

import com.github.not.n0w.weblab3Bot.model.ChatAnswer;
import com.github.not.n0w.weblab3Bot.model.ChatMessage;
import com.github.not.n0w.weblab3Bot.model.Hit;
import com.github.not.n0w.weblab3Bot.model.TelegramBot;
import com.github.not.n0w.weblab3Bot.model.state.ChatState;
import com.github.not.n0w.weblab3Bot.model.state.State;
import com.github.not.n0w.weblab3Bot.service.answerGenerator.AnswerGeneratorService;
import com.github.not.n0w.weblab3Bot.service.answerGenerator.StateHandler;
import com.github.not.n0w.weblab3Bot.service.imageGenerator.ImageGenerator;
import com.github.not.n0w.weblab3Bot.service.transmitterService.TransmitterService;
import com.github.not.n0w.weblab3Bot.utils.ActionRegistryFactory;
import com.github.not.n0w.weblab3Bot.utils.KeyboardFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TelegramBotService {
    private final TelegramBot telegramBot;
    private final TransmitterService transmitterService;
    private AnswerGeneratorService answerGeneratorService;
    private final Map<Long, ChatState> states = new ConcurrentHashMap<>();
    private TemplateEngine templateEngine;
    private KeyboardFactory keyboardFactory;


    public TelegramBotService() {
        this.telegramBot = new TelegramBot(this);
        this.transmitterService = new TransmitterService(this);
        this.templateEngine = new TemplateEngine();
    }

    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".txt");
        resolver.setTemplateMode("TEXT");
        resolver.setCharacterEncoding("UTF-8");
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

    public void startTelegramBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void botStart() {

        templateEngine = createTemplateEngine();
        keyboardFactory = new KeyboardFactory();

        ActionRegistryFactory actionRegistryFactory = new ActionRegistryFactory(
            templateEngine, keyboardFactory, transmitterService
        );

        var handlers = List.of(
                new StateHandler(templateEngine, actionRegistryFactory.createDefaultRegistry(), null, State.DEFAULT),
                new StateHandler(templateEngine, actionRegistryFactory.createXRegistry(), actionRegistryFactory.createXCallbackRegistry(), State.ENTER_X),
                new StateHandler(templateEngine, actionRegistryFactory.createYRegistry(), actionRegistryFactory.createYCallbackRegistry(), State.ENTER_Y),
                new StateHandler(templateEngine, actionRegistryFactory.createRRegistry(), actionRegistryFactory.createRCallbackRegistry(), State.ENTER_R),
                new StateHandler(templateEngine, actionRegistryFactory.createReadyRegistry(), null, State.READY),
                new StateHandler(templateEngine, actionRegistryFactory.createSyncRegistry(), null, State.SYNC)
        );

        this.answerGeneratorService = new AnswerGeneratorService(handlers);

        startTelegramBot();
    }


    public ChatAnswer onMessageReceived(ChatMessage message) {
        if(!states.containsKey(message.getChatId())) {
            ChatState chatState = new ChatState(State.DEFAULT);
            chatState.setChatId(message.getChatId());
            states.put(message.getChatId(),chatState);
        }

        ChatAnswer chatAnswer = answerGeneratorService.generateAnswer(message.getMessage(), states.get(message.getChatId()));
        states.put(message.getChatId(), chatAnswer.getChatState());
        return chatAnswer;
    }

    public void pushUpdatedHitsData(List<Hit> hits, String sessionId, boolean needImage) {
        ChatState chatState = states.values().stream()
                .filter(e -> e.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);
        if(chatState == null) { return; }

        Context context = new Context();
        context.setVariable("hits", hits);

        chatState.toDefault();
        ImageGenerator imageGenerator = new ImageGenerator();
        KeyboardFactory keyboardFactory = new KeyboardFactory();
        telegramBot.outAnswer(
                ChatAnswer.builder()
                        .messageText(templateEngine.process("result", context))
                        .chatState(chatState)
                        .answerChatKeyboard(
                            keyboardFactory.getReplyKeyboardMarkup(State.DEFAULT)
                        )
                        .image(needImage ? imageGenerator.generateHistoryImageFile(hits) : null)
                        .build()

        );

    }


    public ChatAnswer onCallbackReceived(Long chatId, String data) {
        ChatState chatState = states.get(chatId);
        return answerGeneratorService.generateCallbackAnswer(data, chatState);
    }
}