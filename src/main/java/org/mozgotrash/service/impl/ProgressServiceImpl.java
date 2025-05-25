package org.mozgotrash.service.impl;

import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.GoalRepository;
import org.mozgotrash.repository.LogRepository;
import org.mozgotrash.rest.response.BookDto;
import org.mozgotrash.service.ProgressCalculator;
import org.mozgotrash.service.ProgressService;
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
        return getGoalProgressPercentage(userGoals.get(0));
    }

    @Override
    @Transactional
    public BigDecimal getGoalProgressPercentage(Long goalId) {
        Goal goal = goalRepository.findById(goalId).orElseThrow();
        return getGoalProgressPercentage(goal);
    }

    @Override
    public BigDecimal getGoalProgressPercentage(Goal goal) {
        Integer pagesInGoal = goal.getBooks().stream()
                .mapToInt(Book::getPageCount)
                .sum();
        Integer readPages = goal.getBooks().stream()
                .map(book -> {
                        if(book.getStatus().equals(Book.Status.COMPLETED)) {
                            return book.getPageCount();
                        }
                        return logRepository.findAllByBookId(book.getId()).stream().mapToInt(Log::getPageCount).sum();
                })
                .mapToInt(p -> p)
                .sum();
        return progressCalculator.getPercentage(pagesInGoal, readPages);
    }

    @Override
    public BigDecimal getBookProgressPercentage(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        int readPages;
        if(book.getStatus().equals(Book.Status.COMPLETED)) {
            readPages = book.getPageCount();
        } else {
            readPages = logRepository.findAllByBookId(book.getId()).stream().mapToInt(Log::getPageCount).sum();
        }
        return progressCalculator.getPercentage(book.getPageCount(), readPages);
    }

    @Override
    @Transactional
    public BigDecimal logProgress(long bookId, int logPage) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Книга не найдена"));
        logRepository.save(Log.builder().pageCount(logPage).book(book).build());
        Integer pagesInGoal = book.getGoal().getBooks().stream()
                .mapToInt(Book::getPageCount)
                .sum();
        return progressCalculator.getPercentage(pagesInGoal, logPage);
    }
}
