package it.ca.telegrambotserver;

import it.ca.telegrambotserver.telegramBot.bot.Register;
import it.ca.telegrambotserver.telegramBot.bot.repository.StartRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.UserRepository;
import it.ca.telegrambotserver.telegramBot.bot.service.StartService;
import it.ca.telegrambotserver.telegramBot.bot.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegramBotServerApplication {

    public static void main(String[] args) throws TelegramApiException {
        ConfigurableApplicationContext run = SpringApplication.run(TelegramBotServerApplication.class, args);
        UserRepository userRepository = run.getBean(UserRepository.class);
        UserService userService = run.getBean(UserService.class);
        StartService startService = run.getBean(StartService.class);
        StartRepository startRepository = run.getBean(StartRepository.class);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Register(startRepository, startService, userRepository, userService));
    }


}

