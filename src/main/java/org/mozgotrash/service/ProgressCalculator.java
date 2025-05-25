package org.mozgotrash.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class ProgressCalculator {

    public BigDecimal getPercentage(Integer total, Integer readPages) {
        return BigDecimal.valueOf(readPages)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 3, RoundingMode.HALF_DOWN);
    }
}
