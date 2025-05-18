package org.mozgotrash.tg;

import org.mozgotrash.model.Book;
import org.mozgotrash.rest.response.BookDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static org.mozgotrash.constant.TgBotConstant.CHECK_PROGRESS_BUTTON;

public class MarkupFactory {

    public static InlineKeyboardMarkup getInlineKeyboardForBooks(List<Book> books) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for(Book book : books){
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(book.getTitle());
            button.setCallbackData(String.valueOf(book.getId()));
            buttons.add(List.of(button));
        }

        inlineKeyboardMarkup.setKeyboard(buttons);

        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getReplyKeyboardMarkup(List<String> buttons) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Подгоняет размер
        keyboardMarkup.setOneTimeKeyboard(false); // Остается после нажатия

        // Создаем ряды кнопок
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        for(String button : buttons){
            row1.add(button);
        }

        keyboard.add(row1);

        keyboardMarkup.setKeyboard(keyboard);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
