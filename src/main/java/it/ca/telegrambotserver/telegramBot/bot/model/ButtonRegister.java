package it.ca.telegrambotserver.telegramBot.bot.model;

import java.util.*;

public interface ButtonRegister {
    List<String> REGISTER = Arrays.asList("register", "back");
    List<String> BACK = Collections.singletonList("back");
    String BOT_USER_NAME = "@nimedi_bot";
    String BOT_TOKEN = "5333859374:AAFJKMbNoPPr2qO_plS933YDsp0GWkrhjH4";
    String URL = "jdbc:postgresql://localhost:5432/telegram_bot";
    String DB_USERNAME = "postgres";
    String DB_PASSWORD = "root123";
    List<String> BUTTON = Arrays.asList("BackEnd", "FrontEnd", "SMM");
    String PHONE_NUMBER = "+998993393300";
    String TELEGRAM_LINK = "@ncjkdcns";
}
