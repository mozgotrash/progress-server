package org.mozgotrash.rest.response;

import lombok.Builder;
import lombok.Data;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;

import java.math.BigDecimal;

@Data
@Builder
public class ProgressDto {
    GoalDto goal;
    BigDecimal progressPercentage;
}
