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
import java.math.MathContext;
import java.math.RoundingMode;
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


    @Transactional
    public BigDecimal getProgressForGoal(Goal goal) {
        Double pagesInGoal = goal.getBooks().stream()
                .mapToDouble(Book::getPageCount)
                .sum();
        Double pagesRead = goal.getBooks().stream()
                .map(b -> logRepository.findAllByBookId(b.getId()).stream().mapToInt(Log::getPageCount).sum())
                .mapToDouble(p -> p)
                .sum();

        return BigDecimal.valueOf((pagesRead / pagesInGoal) * 100).setScale(3, RoundingMode.FLOOR);
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
