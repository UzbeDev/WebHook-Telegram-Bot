package it.ca.telegrambotserver.telegramBot.bot.service;

import it.ca.telegrambotserver.telegramBot.bot.entity.Comment;
import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResComment;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResUsers;
import it.ca.telegrambotserver.telegramBot.bot.repository.CommentRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.StartRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class UserService {
    public boolean isAdd = false;
    public String txt = "";


    private final UserRepository userRepository;
    private final StartRepository startRepository;
    private final CommentRepository commentRepository;

    public UserService(UserRepository userRepository, StartRepository startRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.startRepository = startRepository;
        this.commentRepository = commentRepository;
    }

    public void saveUser(User user) {

        User save = userRepository.save(user);
        checkStart(save.getChatId());


    }

    public void checkStart(Long chatId) {
        boolean existsUserByChatId = userRepository.existsUserByChatId(chatId);
        if (existsUserByChatId) {
            Start start = startRepository.findStartByChatId(chatId).get();
            startRepository.deleteById(start.getId());
        }
    }

    public List<ResUsers> getOdamlar() {
        List<User> all = userRepository.findAll();
        List<ResUsers> resUsersList = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            ResUsers resUsers = new ResUsers();
            resUsers.setTr(i + 1);
            resUsers.setId(all.get(i).getId());
            resUsers.setChatId(all.get(i).getChatId());
            resUsers.setFirstName(all.get(i).getFirstName());
            resUsers.setLastName(all.get(i).getLastName());
            resUsers.setPhoneNumber(all.get(i).getPhoneNumber());
            resUsersList.add(resUsers);
        }
        return resUsersList;
    }

    public ResUsers getKorishOdam(Integer id) {
        Optional<User> byId = userRepository.findById(id);
        ResUsers resUsers = new ResUsers();
        if (byId.isPresent()) {
            User user = byId.get();
            resUsers.setTgFirstName(user.getTgFirstName());
            resUsers.setTgSurname(user.getTgSurname());
            resUsers.setTgUsername(user.getTgUsername());
        }
        return resUsers;

    }

    public void addComment(String text) {
        Comment comment = new Comment();
        comment.setText(text);
        isAdd = true;
        txt = text;
        commentRepository.save(comment);
    }

    public List<ResComment> getComment() {
        List<Comment> all = commentRepository.findAll();
        List<ResComment> resComments = new ArrayList<>();
        for (Comment comment : all) {
            ResComment resComment = new ResComment();
            resComment.setText(comment.getText());
            resComments.add(resComment);
        }

        return resComments;
    }
}

