package org.mozgotrash.service;

import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

@Service
public class ProgressCalculator {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LogRepository logRepository;

    /**
     * Расчет прогресса завершения цели по формуле
     * res = (% прогресса книга1 + % прогресса книга2 + % прогресса книга3) / 3
     * res = (100 + 50 + 0) / 3 = 50%
     * @param goal
     * return процент достижения цели
     *
     */
    //TODO remove Transactional
    //проблем с LOB
    @Transactional
    public double getProgressForGoal(Goal goal) {
        Double pagesInGoal = goal.getBooks().stream()
                .mapToDouble(Book::getPageCount)
                .sum();
        Double pagesRead = goal.getBooks().stream()
                .map(b -> logRepository.findAllByBookId(b.getId()).stream().mapToInt(Log::getPageCount).sum())
                .mapToDouble(p -> p)
                .sum();

        return (pagesRead / pagesInGoal) * 100;
    }


    @Transactional
    public double getProgressForBook(Book book) {
        int readPages;
        if(book.getStatus().equals(Book.Status.COMPLETED)) {
            readPages = book.getPageCount();
        } else {
            readPages = logRepository.findAllByBookId(book.getId()).stream().mapToInt(Log::getPageCount).sum();
        }
        return  ((double) readPages / book.getPageCount()) * 100;
    }
}
