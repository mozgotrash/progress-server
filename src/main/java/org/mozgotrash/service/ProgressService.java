package org.mozgotrash.service;

import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.model.User;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.GoalRepository;
import org.mozgotrash.repository.LogRepository;
import org.mozgotrash.rest.response.GoalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProgressService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ProgressCalculator progressCalculator;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public BigDecimal getProgressPercentage(Long userId) {
        List<Goal> userGoals = goalRepository.findByUserId(userId);
        return getProgressPercentage(userGoals.get(0));
    }

    @Transactional
    public BigDecimal getProgressPercentage(Goal goal) {
        return progressCalculator.getProgressForGoal(goal);

    }

    public void logProgress(Integer logPage) {
        Book book = bookRepository.findById(2L).get();
        logRepository.save(Log.builder().pageCount(logPage).book(book).build());
    }
}
