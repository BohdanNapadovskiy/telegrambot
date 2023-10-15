package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.CryptoMessage;
import org.example.service.MessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Component
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final BotMessageService botSender;


    @Override
    public void sendMessage(CryptoMessage message) {
        SendMessage sendMessage =
            SendMessage.builder()
            .text(message.getMessageText())
            .chatId(String.valueOf(message.getChatId()))
            .parseMode(ParseMode.HTML)
            .replyMarkup(null)
            .build();
        execute(sendMessage);
    }


    private void execute(BotApiMethod botApiMethod) {
        try {
            botSender.execute(botApiMethod);
        } catch (Exception e) {
//            log.error("Exception: ", e);
        }
    }



}
