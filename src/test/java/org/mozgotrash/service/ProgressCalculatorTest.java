package org.mozgotrash.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.LogRepository;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ProgressCalculatorTest {

    ProgressCalculator progressCalculator = new ProgressCalculator();

    @Test
    void bookProgressPercentage() {
        var logs = List.of(
                Log.builder().pageCount(3).build(),
                Log.builder().pageCount(2).build(),
                Log.builder().pageCount(5).build());
        Book book = Book.builder()
                .status(Book.Status.IN_PROGRESS)
                .pageCount(500)
                .build();
        var res = progressCalculator.getProgressForBook(logs, book);
        Assertions.assertEquals(2d, res);
    }

    @Test
    void goalProgressPercentage() {
        var logsByBook1 = List.of(
                Log.builder().pageCount(100).build(),
                Log.builder().pageCount(50).build(),
                Log.builder().pageCount(50).build());

        var logsByBook2 = List.of(
                Log.builder().pageCount(50).build());

        Map<Book, List<Log>> logsByBook = Map.of(
                //book1 100% прочитано
                Book.builder().id(1l).status(Book.Status.IN_PROGRESS).pageCount(200).build(), logsByBook1,
                //book2 50% прочитано
                Book.builder().id(2l).status(Book.Status.HOLD).pageCount(100).build(), logsByBook2,
                //book3 0% прочитано
                Book.builder().id(3l).status(Book.Status.HOLD).pageCount(100).build(), List.of()
        );

        double res = progressCalculator.getProgressForGoal(logsByBook);
        Assertions.assertEquals(50d, res);
    }
}


