package org.mozgotrash.service;

import org.mozgotrash.model.User;
import org.mozgotrash.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TgBot extends TelegramLongPollingBot {

    @Autowired
    private ProgressService progressService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String getBotUsername() {
        return "log_progress_bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();

        if(update.hasCallbackQuery()) {
            if(update.getCallbackQuery().getData().equals("button_isThereProgress")) {
                message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                User user = userRepository.findByTgId(update.getCallbackQuery().getFrom().getId());
                BigDecimal percentage = progressService.getProgressPercentage(user.getId());
                message.setText("Текущий прогресс: " + percentage + "%");
            }
        }


        if(update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                message.setChatId(String.valueOf(chatId));
                message.setText("Выберите действие:");
                message.setReplyMarkup(getInlineKeyboardMarkup());


            } else if(messageText.contains("Залогировать прогресс")) {
                message.setChatId(String.valueOf(chatId));
                message.setText("Введите количество страниц ");

            } else if(messageText.contains("Узнать текущий прогресс")) {
                message.setChatId(String.valueOf(chatId));
                User user = userRepository.findByTgId(update.getMessage().getFrom().getId());
                BigDecimal percentage = progressService.getProgressPercentage(user.getId());
                message.setText("Текущий прогресс: " + percentage);
            } else {
                progressService.logProgress(Integer.parseInt(messageText));
                message.setChatId(String.valueOf(chatId));
                message.setText("Успешно");
            }
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    // Создаем клавиатуру
    public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Подгоняет размер
        keyboardMarkup.setOneTimeKeyboard(false); // Остается после нажатия

        // Создаем ряды кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первый ряд
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Есть прогресс?");
        row1.add("Залогировать прогресс");

        keyboard.add(row1);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Есть прогресс?");
        button1.setCallbackData("button_isThereProgress");

        inlineKeyboardMarkup.setKeyboard(List.of(List.of(button1)));

        return inlineKeyboardMarkup;
    }
}
