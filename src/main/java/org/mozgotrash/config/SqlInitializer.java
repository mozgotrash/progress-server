package org.mozgotrash.config;

import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

//TODO временный класс для разработки, удалить после
@Component
public class SqlInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GoalRepository goalRepository;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Goal saved = goalRepository.save(Goal.builder()
                .title("Java senior")
                .deadline(LocalDate.parse("2027-10-23"))
                .build());

        Book book1 = bookRepository.save(Book.builder()
                .pageCount(630)
                .imageData(getImageBytes("persistense.png"))
                .title("Java Persistence API и Hibernate")
                .author("Кристиан Бауэр, Гэри Грегори, Гэвин Кинг")
                .goal(saved)
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