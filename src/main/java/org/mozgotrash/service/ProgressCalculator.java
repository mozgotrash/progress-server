package org.mozgotrash.service;

import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public double getProgressForGoal(Map<Book, List<Log>> bookAndLogs) {
        List<Double> progressList = new ArrayList<>();
        bookAndLogs.forEach((book, logs) -> progressList.add(
                getProgressForBook(
                        logs,
                        book.getPageCount()
                )
        ));

        return progressList.stream().mapToDouble(Double::doubleValue).sum() / progressList.size();
    }


    public double getProgressForBook(List<Log> logs, Integer bookPageCount) {
        int readPages = logs.stream().mapToInt(Log::getPageCount).sum();
        return  ((double) readPages / bookPageCount) * 100;
    }

}
