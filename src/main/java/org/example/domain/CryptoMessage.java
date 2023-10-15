package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CryptoMessage {

    private Long chatId;
    private Long userId;
    private String messageText;

    public static CryptoMessage prepareMessage(UserRequest request, String text) {
        return CryptoMessage.builder()
            .chatId(request.getChatId())
            .userId(request.getUserId())
            .messageText(text)
            .build();

    }
}
