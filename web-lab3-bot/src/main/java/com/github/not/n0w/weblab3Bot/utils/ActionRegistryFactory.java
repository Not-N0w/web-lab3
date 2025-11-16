package com.github.not.n0w.weblab3Bot.utils;

import com.github.not.n0w.weblab3Bot.model.action.ActionRegistry;
import com.github.not.n0w.weblab3Bot.model.action.actions.*;
import com.github.not.n0w.weblab3Bot.model.action.actions.build.*;
import com.github.not.n0w.weblab3Bot.model.action.actions.validator.XValidator;
import com.github.not.n0w.weblab3Bot.model.action.actions.validator.YValidator;
import com.github.not.n0w.weblab3Bot.service.transmitterService.TransmitterService;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;

public class ActionRegistryFactory {

    private final TemplateEngine templateEngine;
    private final KeyboardFactory keyboardFactory;
    private final TransmitterService transmitterService;

    private final XBuildAction xBuildAction;
    private final YBuildAction yBuildAction;
    private final RBuildAction rBuildAction;
    private final DefaultBuildAction defaultBuildAction;
    private final ReadyBuildAction readyBuildAction;
    private final SyncBuildAction syncBuildAction;

    public ActionRegistryFactory(
            TemplateEngine templateEngine,
            KeyboardFactory keyboardFactory,
            TransmitterService transmitterService
    ) {
        this.templateEngine = templateEngine;
        this.keyboardFactory = keyboardFactory;
        this.transmitterService = transmitterService;

        this.xBuildAction = new XBuildAction(keyboardFactory);
        this.yBuildAction = new YBuildAction(keyboardFactory);
        this.rBuildAction = new RBuildAction(keyboardFactory);
        this.defaultBuildAction = new DefaultBuildAction(templateEngine, keyboardFactory);
        this.readyBuildAction = new ReadyBuildAction(templateEngine, keyboardFactory);
        this.syncBuildAction = new SyncBuildAction(keyboardFactory);
    }

    public ActionRegistry createDefaultRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new NotRecognizedAction(templateEngine));

        registry.addAction("/start", defaultBuildAction);
        registry.addAction("Hit", new TransferStateAction(xBuildAction, keyboardFactory));
        registry.addAction("Show hits", new GetAllHitsAction(transmitterService));
        registry.addAction("Clear hits", new ClearAllHitsAction(transmitterService, keyboardFactory));
        registry.addAction("Set session", new TransferStateAction(syncBuildAction, keyboardFactory));

        return registry;
    }

    public ActionRegistry createXRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new NotRecognizedAction(templateEngine));

        registry.addAction("Назад", new TransferStateAction(defaultBuildAction, keyboardFactory));
        registry.addAction("Далее", new TransferStateAction(yBuildAction, keyboardFactory, new XValidator()));

        return registry;
    }

    public ActionRegistry createXCallbackRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new NotRecognizedAction(templateEngine));

        for (int i = -5; i <= 5; i++) {
            registry.addAction(String.valueOf(i), new ToggleXAction(keyboardFactory));
        }

        return registry;
    }


    public ActionRegistry createYRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new YSetAction(keyboardFactory));

        registry.addAction("Назад", new TransferStateAction(xBuildAction, keyboardFactory));
        registry.addAction("Далее", new TransferStateAction(rBuildAction, keyboardFactory, new YValidator()));

        return registry;
    }

    public ActionRegistry createYCallbackRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new NotRecognizedAction(templateEngine));
        return registry;
    }

    public ActionRegistry createRRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new NotRecognizedAction(templateEngine));

        registry.addAction("Назад", new TransferStateAction(yBuildAction, keyboardFactory));
        registry.addAction("Далее", new TransferStateAction(readyBuildAction, keyboardFactory));

        return registry;
    }

    public ActionRegistry createRCallbackRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new NotRecognizedAction(templateEngine));

        for (double i = 1; i <= 3; i += 0.5) {
            registry.addAction(String.valueOf(i), new ChangeRAction(keyboardFactory));
        }

        return registry;
    }

    public ActionRegistry createReadyRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new NotRecognizedAction(templateEngine));

        registry.addAction("Назад", new TransferStateAction(rBuildAction, keyboardFactory));
        registry.addAction("Да", new SendAction(transmitterService));

        return registry;
    }

    public ActionRegistry createSyncRegistry() {
        ActionRegistry registry = new ActionRegistry();
        registry.setDefaultAction(new SessionIdSetAction(keyboardFactory, transmitterService, templateEngine));

        registry.addAction("Ок", new TransferStateAction(defaultBuildAction, keyboardFactory));
        registry.addAction("Назад", new TransferStateAction(defaultBuildAction, keyboardFactory));

        return registry;
    }
}
