package org.mozgotrash.config;

import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.GoalRepository;
import org.mozgotrash.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.OffsetDateTime;

//TODO временный класс для разработки, удалить после
@Component
public class SqlInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private LogRepository logRepository;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Goal saved = goalRepository.save(Goal.builder()
                .title("Java senior")
                .deadline(LocalDate.parse("2027-10-23"))
                .build());
        bookRepository.save(Book.builder()
                .pageCount(526)
                .imageData(getImageBytes("spring-in-actions.png"))
                .title("Spring в действии, 6-е издание")
                .author("Крейг Уоллс")
                .goal(saved)
                .status(Book.Status.COMPLETED)
                .build());
        Book bookWithLogs = bookRepository.save(Book.builder()
                .pageCount(630)
                .imageData(getImageBytes("persistense.png"))
                .title("Java Persistence API и Hibernate")
                .author("Кристиан Бауэр, Гэри Грегори, Гэвин Кинг")
                .goal(saved)
                .status(Book.Status.IN_PROGRESS)
                .build());
        bookRepository.save(Book.builder()
                .pageCount(836)
                .imageData(getImageBytes("algorithms.png"))
                .title("Алгоритмы на Java, 4-е издание")
                .author("Роберт Седжвик, Кевин Уэйн")
                .goal(saved)
                .status(Book.Status.HOLD)
                .build());

        logRepository.save(Log.builder()
                .book(bookWithLogs)
                .pageCount(180)
                .logDate(OffsetDateTime.now())
                .build());
    }

    private byte[] getImageBytes(String filename) throws IOException {
        InputStream inputStream = SqlInitializer.class.getClassLoader()
                .getResourceAsStream("img/" + filename);

        if (inputStream == null) {
            throw new IOException("File not found in resources: png/" + filename);
        }

        return inputStream.readAllBytes();
    }
}