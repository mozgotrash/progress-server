package org.mozgotrash.tg;

import org.mozgotrash.constant.BotState;
import org.mozgotrash.constant.TgBotConstant;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.User;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.LogRepository;
import org.mozgotrash.repository.UserRepository;
import org.mozgotrash.repository.dto.GoalLogs;
import org.mozgotrash.service.ProgressService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
public class TgBot extends TelegramLongPollingBot {
    private final ProgressService progressService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LogRepository logRepository;
    private final UserStateManager userStateManager = new UserStateManager();


    public TgBot(@Value("${tg.bot.token}") String token,
                 ProgressService progressService,
                 UserRepository userRepository,
                 BookRepository bookRepository,
                 LogRepository logRepository) {
        super(token);
        this.progressService = progressService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.logRepository = logRepository;
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
            boolean isAdmin = Optional.ofNullable(update.getMessage().getFrom().getUserName()).orElse("").equals(TgBotConstant.ADMIN_USERNAME.getCode());

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
                    } else if (message.equals("Прочитал")) {
                        if(!isAdmin) {
                            sendMessage.setText("У вас пока нет права логировать прогресс");
                            break;
                        }
                        Book book = bookRepository.findById(2L).get();
                        sendMessage.setText("Для какой книги отметить прогресс?");
                        sendMessage.setReplyMarkup(MarkupFactory.getInlineKeyboardForBooks(List.of(book)));
                        userStateManager.setState(userId, BotState.AWAITING_BOOK_ID);
                    } else if (message.equals("Есть прогресс?")) {
                        User user = userRepository.findByTgId(637781634L);
                        BigDecimal percentage = progressService.getProgressPercentage(user.getId());
                        List<GoalLogs> goalLogs = logRepository.getLogsByGoalsForUser(user.getId(), LocalDateTime.now().minusDays(3));
                        String logInfo = String.format("За последние 3 дня в проекте %s прочитано %d страниц",
                                goalLogs.get(0).goalTitle(), goalLogs.get(0).pageCount());
                        sendMessage.setText("Текущий прогресс: " + percentage + "%\n" + logInfo);
                    }
                }
                case AWAITING_PAGES -> {
                    if (!message.matches("-?\\d+")) {
                        sendMessage.setText("Введи корректное число");
                        break;
                    }
                    BigDecimal gainProgress = progressService.logProgress(userStateManager.getBookId(userId), Integer.parseInt(message));
                    userStateManager.setState(userId, BotState.START);
                    sendMessage.setText(String.format("Как с куста +%s%s", gainProgress, "%"));

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
