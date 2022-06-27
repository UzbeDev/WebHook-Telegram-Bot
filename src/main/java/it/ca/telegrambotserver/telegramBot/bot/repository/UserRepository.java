package it.ca.telegrambotserver.telegramBot.bot.repository;

import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUserById(Integer id);
    boolean existsUserByChatId(Long chatId);
}
