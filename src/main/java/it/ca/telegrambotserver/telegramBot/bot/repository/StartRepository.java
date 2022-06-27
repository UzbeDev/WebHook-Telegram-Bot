package it.ca.telegrambotserver.telegramBot.bot.repository;

import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StartRepository extends JpaRepository<Start, Integer> {
//    boolean existsStartByUsernameEqualsIgnoreCaseAndChatId(String username, Long chatId);

    Optional<Start> findStartByChatId(Long chatId);
}
