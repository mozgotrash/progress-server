package org.mozgotrash.tg;

import org.mozgotrash.constant.BotState;
import org.mozgotrash.constant.TgBotConstant;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.User;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.UserRepository;
import org.mozgotrash.rest.response.BookDto;
import org.mozgotrash.service.impl.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static org.mozgotrash.constant.TgBotConstant.CHECK_PROGRESS_BUTTON;

@Component
public class TgBot extends TelegramLongPollingBot {
    private final ProgressService progressService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserStateManager userStateManager = new UserStateManager();


    public TgBot(@Value("${tg.bot.token}") String token,
                 ProgressService progressService,
                 UserRepository userRepository,
                 BookRepository bookRepository) {
        super(token);
        this.progressService = progressService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public String getBotUsername() {
        return "log_progress_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        Long chatId = null;
        BotState botState;


        if (update.hasCallbackQuery()) {
            botState = userStateManager.getState(update.getCallbackQuery().getFrom().getId());
            String callbackData = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            Long userId = update.getCallbackQuery().getFrom().getId();
            switch (botState) {
                case AWAITING_BOOK_ID -> {
                    userStateManager.setUserBookId(userId, Long.parseLong(callbackData));
                    userStateManager.setState(userId, BotState.AWAITING_PAGES);
                    sendMessage.setText("Введи колличество прочитанных страниц");
                }

            }
        }


        if (update.hasMessage() && update.getMessage().hasText()) {
            botState = userStateManager.getState(update.getMessage().getFrom().getId());
            chatId = update.getMessage().getChatId();
            Long userId = update.getMessage().getFrom().getId();
            boolean isAdmin = update.getMessage().getFrom().getUserName().equals(TgBotConstant.ADMIN_USERNAME.getCode());

            //попробовать отдельно стейт апдейтить и сообщения
            // State machine ???
            String message = update.getMessage().getText();
            switch (botState) {
                case START -> {
                    if (message.equals("/start")) {
                        if (isAdmin) {
                            sendMessage.setReplyMarkup(MarkupFactory.getReplyKeyboardMarkup(List.of("Прочитал", "Есть прогресс?")));
                            sendMessage.setText("Привет, я бот на службе прогресса Кости.");
                        } else {
                            sendMessage.setReplyMarkup(MarkupFactory.getReplyKeyboardMarkup(List.of("Есть прогресс?")));
                            sendMessage.setText("Привет, я бот на службе прогресса Кости.");
                        }
                    } else if (message.equals("Прочитал") && isAdmin) {
                        Book book = bookRepository.findById(2L).get();
                        sendMessage.setText("Для какой книги отметить прогресс?");
                        sendMessage.setReplyMarkup(MarkupFactory.getInlineKeyboardForBooks(List.of(book)));

                        userStateManager.setState(userId, BotState.AWAITING_BOOK_ID);
                    } else if (message.equals("Есть прогресс?")) {
                        User user = userRepository.findByTgId(update.getMessage().getFrom().getId());
                        BigDecimal percentage = progressService.getProgressPercentage(user.getId());
                        sendMessage.setText("Текущий прогресс: " + percentage + "%");
                    }
                }
                case AWAITING_PAGES -> {
                    if (!message.matches("-?\\d+")) {
                        sendMessage.setText("Введи корректное число");
                    }
                    progressService.logProgress(userStateManager.getBookId(userId), Integer.parseInt(message));
                    userStateManager.setState(userId, BotState.START);
                    sendMessage.setText("Готово");

                    if (isAdmin) {
                        sendMessage.setReplyMarkup(MarkupFactory.getReplyKeyboardMarkup(List.of("Прочитал", "Есть прогресс?")));
                    } else {
                        sendMessage.setReplyMarkup(MarkupFactory.getReplyKeyboardMarkup(List.of("Есть прогресс?")));
                    }
                }

            }
        }
        sendMessage.setChatId(chatId);
        execute(sendMessage);
    }

    private void execute(SendMessage message) {
        try {
            super.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
