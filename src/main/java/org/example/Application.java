package org.example;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
@EnableJpaRepositories
public class Application {
//    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws TelegramApiException {
        SpringApplication.run(Application.class);
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        try {
//            System.out.println("Registering bot...");
//            telegramBotsApi.registerBot(new TelegramBot());
//        } catch (TelegramApiRequestException e) {
//            System.out.println("Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).");
////            log.error("Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).", e);
//        }
    }
}