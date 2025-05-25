package org.mozgotrash.service;

import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;

import java.math.BigDecimal;

public interface ProgressService {
    BigDecimal getProgressPercentage(Long userId);

    BigDecimal getGoalProgressPercentage(Long goalId);

    BigDecimal getGoalProgressPercentage(Goal goal);

    BigDecimal getBookProgressPercentage(Long goalId);

    BigDecimal logProgress(long bookId, int logPageCount);
}
