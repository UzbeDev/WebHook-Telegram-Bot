package it.ca.telegrambotserver.telegramBot.bot;

import com.fasterxml.jackson.core.io.InputDecorator;
import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import it.ca.telegrambotserver.telegramBot.bot.model.ButtonRegister;
import it.ca.telegrambotserver.telegramBot.bot.model.InlineButton;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResStart;
import it.ca.telegrambotserver.telegramBot.bot.payload.ResUsers;
import it.ca.telegrambotserver.telegramBot.bot.repository.StartRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.UserRepository;
import it.ca.telegrambotserver.telegramBot.bot.service.StartService;
import it.ca.telegrambotserver.telegramBot.bot.service.UserService;
import lombok.SneakyThrows;
import org.hibernate.procedure.internal.PostgresCallableStatementSupport;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.io.File;
import java.util.*;

@Service
public class Register extends TelegramLongPollingBot {

    //    @Autowired
//    UserService userService;


    private final StartRepository startRepository;

    private final StartService startService;

    private final UserRepository userRepository;

    private final UserService userService;


    private static final InlineButton IB = new InlineButton();
    Map<Long, String> start = new HashMap<>();
    Map<Long, String> isRegister = new HashMap<>();
    Map<Long, String> lastNameUser = new HashMap<>();
    Map<Long, String> firstNameUser = new HashMap<>();
    Map<Long, String> phoneNumberUser = new HashMap<>();
    Map<Long, String> registerUser = new HashMap<>();
    Set<Long> statestika = new HashSet<>();

    Long chatId1;
    String name;
    String lastName;
    String phoneNumber;

    public Register(StartRepository startRepository, StartService startService, UserRepository userRepository, UserService userService) {
        this.startRepository = startRepository;
        this.startService = startService;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public void onUpdateReceived(Update update) {
//        UserService userService = new UserService(userRepository);
        if (update.hasMessage()) {
            SendMessage sendMessage = new SendMessage();
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            SendPhoto sendPhoto = new SendPhoto();
            if (message.hasText()) {
                String text = message.getText();
                String firstName = message.getFrom().getFirstName();
                sendMessage.setChatId(chatId.toString());
                if (userService.isAdd) {
                    List<ResUsers> odamlar = userService.getOdamlar();
                    for (int i = 0; i < odamlar.size(); i++) {
                        Long chatId2 = odamlar.get(i).getChatId();
                        sendMessage.setChatId(String.valueOf(chatId2));
                        sendMSG(sendMessage, userService.txt);
                    }
                    userService.isAdd = false;
                }
                if (startService.isAdd1) {
                    List<ResStart> odamlar1 = startService.getStart();
                    for (int i = 0; i < odamlar1.size(); i++) {
                        sendMessage.setChatId(String.valueOf(chatId1));
                        sendMSG(sendMessage, startService.txt1);
//                        SendPhoto.builder().build().setPhoto(new InputFile(startService.rasm1));

                        sendPhotoMsg(String.valueOf(chatId), startService.rasm1);
                    }

                    startService.isAdd1 = false;
                }

                if (text.equals("/start")) {
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup(getInlineButtonRows(ButtonRegister.BUTTON));
                    sendMessage.setReplyMarkup(markup);
                    start.put(chatId, firstName);
                    sendMSG(sendMessage, "ismingizni kirining");
                    isRegister.put(chatId, "firstName");
                    statestika.add(chatId + 1);
                    chatId1 = chatId;
                    System.err.println(chatId1);
                    Start start = new Start(chatId, update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getUserName());
                    startService.saveStart(start);
                    startRepository.save(start);

                } else if (text.equals("Kurslar")) {
                    getRegister(sendMessage);
                    sendMSG(sendMessage, "kurslar");
                } else if (text.equals("bizbilan aloqa")) {
                    sendMSG(sendMessage, "phoneNumber: " + ButtonRegister.PHONE_NUMBER + "\ntelegrem link: " +
                            ButtonRegister.TELEGRAM_LINK);
                } else if (text.equals("statestika")) {
                    sendMSG(sendMessage, "statestika: " + statestika.size());
                } else if (isRegister.get(chatId).equals("firstName")) {
                    isRegister.put(chatId, "lastName");
                    name = text;
                    sendMSG(sendMessage, "famliyangizni kiriting");
                    firstNameUser.put(chatId, text);
                    System.err.println(firstNameUser);
                    System.err.println(name);
                } else if (isRegister.get(chatId).equals("lastName")) {
                    isRegister.put(chatId, "phoneNumber");
                    lastName = text;
                    sendMSG(sendMessage, "kiritng");
                    lastNameUser.put(chatId, text);
                    System.err.println(lastName);
                    System.err.println(lastNameUser);
                    buttonPhoneNumber(sendMessage);
                } else if (isRegister.get(chatId).equals("phoneNumber")) {
                    buttonInfo(sendMessage);
                    phoneNumberUser.put(chatId, text);
                    phoneNumber = text;
                    System.err.println(phoneNumber);
                    System.err.println(phoneNumberUser);
//                    sendMSG(sendMessage, "firstName: " + firstNameUser.get(chatId) + "\nlastName: " + lastNameUser.get(chatId)
//                            + "\nphoneNUmber: " + phoneNumberUser.get(chatId));
//                    userService.addUsers(chatId1, name, lastName, phoneNumber);
                    isRegister.remove(chatId);

                    User user = new User(chatId, firstNameUser.get(chatId), lastNameUser.get(chatId), phoneNumberUser.get(chatId), update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName(), update.getMessage().getFrom().getUserName());
                    userService.saveUser(user);
                    System.out.println(user);
                    userRepository.save(user);
                    userService.getComment();
                }
            } else if (message.hasContact()) {
                Contact contact = message.getContact();
                buttonInfo(sendMessage);
                phoneNumberUser.put(chatId, contact.getPhoneNumber());
                phoneNumber = contact.getPhoneNumber();
                System.err.println(phoneNumber);
                System.err.println(phoneNumberUser);
//                    sendMSG(sendMessage, "firstName: " + firstNameUser.get(chatId) + "\nlastName: " + lastNameUser.get(chatId)
//                            + "\nphoneNUmber: " + phoneNumberUser.get(chatId));
//                    userService.addUsers(chatId1, name, lastName, phoneNumber);
                isRegister.remove(chatId);

                User user = new User(chatId, firstNameUser.get(chatId), lastNameUser.get(chatId), phoneNumberUser.get(chatId), update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName(), update.getMessage().getFrom().getUserName());
                userService.saveUser(user);
                System.out.println(user);
                userRepository.save(user);
                userService.getComment();
            }

        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long id = update.getCallbackQuery().getFrom().getId();
            SendMessage message = new SendMessage();
            message.setChatId(id.toString());
            switch (data) {
                case "BackEnd":
                    getRegister(message);
                    sendMSG(message, "bla bla");
                    registerUser.put(id, data);
                    break;
                case "FrontEnd":
                    getRegister(message);
                    sendMSG(message, "frontEnd bla bal");
                    registerUser.put(id, data);
                    break;
                case "SMM":
                    getRegister(message);
                    sendMSG(message, "SMM bla bola");
                    registerUser.put(id, data);
                    break;
                case "back":
                    getRegisterMenu(message);
                    registerUser.put(id, null);
                    sendMSG(message, "yunalishni tanlang");
                    break;
//                case "back end":
//                    back(message);
//                    sendMSG(message, "bla bla");
//                    registerUser.put(id, data);
//                    break;
//                case "front end":
//                    back(message);
//                    sendMSG(message, "frontEnd bla bal");
//                    registerUser.put(id, data);
//                    break;
//                case "Smm":
//                    back(message);
//                    sendMSG(message, "SMM bla bola");
//                    registerUser.put(id, data);
//                    break;
                case "register":
                    sendMSG(message, "ismingizni kiriting");
                    isRegister.put(id, "firstName");
                    break;
            }
        }
    }


    public void getRegister(SendMessage message) {
        message.setParseMode(ParseMode.MARKDOWN);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(getInlineButtonRows(ButtonRegister.REGISTER));
        message.setReplyMarkup(markup);
    }


    public void getRegisterMenu(SendMessage message) {
        message.setParseMode(ParseMode.MARKDOWN);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(getInlineButtonRows(ButtonRegister.BUTTON));
        message.setReplyMarkup(markup);
    }


    public void sendMSG(SendMessage message, String text) {
        try {
            message.setText(text);
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("not execute");
        }
    }

    public void sendPhotoMsg(String chatId, String rasm) {
        // Create send method
        SendPhoto sendPhotoRequest = new SendPhoto();
        // Set destination chat id
        sendPhotoRequest.setChatId(chatId);
        // Set the photo file as a new photo (You can also use InputStream with a constructor overload)
        sendPhotoRequest.setPhoto(new InputFile(new File("C:\\Users\\hcode\\Downloads\\templatemo_576_snapx_photography\\templatemo_576_snapx_photography\\assets\\images\\author.jpg")));
        try {
            // Execute the method
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            System.err.println("not execute");
        }
    }

//    public void sendPhotoMsg(String rasm) {
//        try {
//
//                final File initialFile = new File(String.valueOf(rasm));
//                final InputStream targetStream =
//                        new DataInputStream(new FileInputStream(initialFile));
//                SendPhoto sendPhoto = new SendPhoto();
//                sendPhoto.setPhoto(new InputFile(String.valueOf(targetStream)));
//            this.execute(sendPhoto);
//
//        } catch (FileNotFoundException | TelegramApiException e) {
//            System.out.println("not execute");
//        }
//    }


    public void buttonPhoneNumber(SendMessage message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("telifon raqamingizni kiriting");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMSG(message, "Phone number");
    }

    public List<List<InlineKeyboardButton>> getInlineButtonRows(List<String> data) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int length = data.size() % 2 != 0 ? data.size() - 1 : data.size();
        for (int i = 0; i < length; i += 2) {
            List<InlineKeyboardButton> inlineButton = new ArrayList<>();
            inlineButton.add(IB.getInlineButton(data.get(i), data.get(i)));
            inlineButton.add(IB.getInlineButton(data.get(i + 1), data.get(i + 1)));
            rows.add(inlineButton);
        }
        if (data.size() % 2 != 0) {
            String text = data.get(data.size() - 1);
            rows.add(Collections.singletonList(IB.getInlineButton(text, text)));
        }
        return rows;
    }

    public static void buttonInfo(SendMessage sendMessage) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Kurslar");
        row.add("statestika");
        KeyboardRow row1 = new KeyboardRow();
        row1.add("bizbilan aloqa");
        keyboard.add(row);
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        keyboardMarkup.setOneTimeKeyboard(true);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    @Override
    public String getBotUsername() {
        return "@nimedi_bot";
    }

    @Override
    public String getBotToken() {
        return "5333859374:AAFJKMbNoPPr2qO_plS933YDsp0GWkrhjH4";
    }
}
