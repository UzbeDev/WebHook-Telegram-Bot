package it.ca.telegrambotserver.telegramBot.bot.repository;

import it.ca.telegrambotserver.telegramBot.bot.entity.Comment;
import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
