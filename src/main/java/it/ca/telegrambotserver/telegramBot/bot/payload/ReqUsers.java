package it.ca.telegrambotserver.telegramBot.bot.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUsers {
    private Long chatId;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
