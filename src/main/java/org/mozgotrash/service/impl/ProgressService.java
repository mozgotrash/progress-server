package org.mozgotrash.service.impl;

import org.mozgotrash.model.Goal;

import java.math.BigDecimal;

public interface ProgressService {
    BigDecimal getProgressPercentage(Long userId);

    BigDecimal getProgressPercentage(Goal goal);

    void logProgress(long bookId, int logPageCount);
}
