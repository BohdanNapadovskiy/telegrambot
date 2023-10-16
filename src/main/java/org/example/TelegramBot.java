package org.example;

import lombok.RequiredArgsConstructor;
import org.example.domain.UserRequest;
import org.example.service.CommandDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

   private final CommandDispatcher dispatcher;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String textFromUser = update.getMessage().getText();
            Long userId = update.getMessage().getFrom().getId();
            String userFirstName = update.getMessage().getFrom().getFirstName();
            log.info("[{}, {}] : {}", userId, userFirstName, textFromUser);
            Long chatId = update.getMessage().getChatId();
            UserRequest userRequest = new UserRequest(textFromUser, chatId, userId);
            if(dispatcher.exitCommand(userRequest)) {
                dispatcher.executeCommandByRequest(userRequest);
            } else {
                log.error("Unexpected update from user");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
