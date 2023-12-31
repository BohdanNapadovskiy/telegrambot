package org.example.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class BotMessageService extends DefaultAbsSender {

    @Value("${bot.token}")
    private String botToken;

    protected BotMessageService() {
        super(new DefaultBotOptions());
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }
}
