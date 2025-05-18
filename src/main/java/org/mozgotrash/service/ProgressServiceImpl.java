package org.mozgotrash.service;

import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.GoalRepository;
import org.mozgotrash.repository.LogRepository;
import org.mozgotrash.service.impl.ProgressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final GoalRepository goalRepository;
    private final LogRepository logRepository;
    private final ProgressCalculator progressCalculator;
    private final BookRepository bookRepository;

    public ProgressServiceImpl(GoalRepository goalRepository, LogRepository logRepository, ProgressCalculator progressCalculator, BookRepository bookRepository) {
        this.goalRepository = goalRepository;
        this.logRepository = logRepository;
        this.progressCalculator = progressCalculator;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public BigDecimal getProgressPercentage(Long userId) {
        List<Goal> userGoals = goalRepository.findByUserId(userId);
        return getProgressPercentage(userGoals.get(0));
    }

    @Override
    @Transactional
    public BigDecimal getProgressPercentage(Goal goal) {
        return progressCalculator.getProgressForGoal(goal);
    }

    @Override
    public void logProgress(long bookId, int logPage) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Книга не найдена"));
        logRepository.save(Log.builder().pageCount(logPage).book(book).build());
    }
}
