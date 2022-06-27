package it.ca.telegrambotserver.telegramBot.bot.service;

import it.ca.telegrambotserver.telegramBot.bot.entity.Comment;
import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResComment;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResStart;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResStart;
import it.ca.telegrambotserver.telegramBot.bot.repository.CommentRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.StartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service

public class StartService {
    public boolean isAdd1 = false;
    public String txt1 = "";
    public String rasm1;

    @Autowired
    CommentRepository commentRepository;

    private final StartRepository startRepository;

    public StartService(StartRepository startRepository) {
        this.startRepository = startRepository;
    }

    public void saveStart(Start start) {
            startRepository.save(start);
    }

    public List<ResStart> getStart(){
        List<Start> all = startRepository.findAll();
        List<ResStart> resStartsList = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            ResStart resStarts = new ResStart();
            resStarts.setTr(i+1);
            resStarts.setChatId(all.get(i).getChatId());
            resStarts.setFirstName(all.get(i).getFirstName());
            resStarts.setUsername(all.get(i).getUsername());
            resStartsList.add(resStarts);
        }
        return resStartsList;
    }

    public void addComment(ResComment resComment){
        Comment comment = new Comment();
        comment.setText(resComment.getText());
        comment.setRasm(resComment.getRasm());
        isAdd1=true;
        txt1= resComment.getText();
        rasm1= resComment.getRasm();
        commentRepository.save(comment);
    }

    public List<ResComment> getComment(){
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

