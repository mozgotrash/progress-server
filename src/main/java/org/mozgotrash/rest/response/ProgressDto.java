package org.mozgotrash.rest.response;

import lombok.Builder;
import lombok.Data;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;

@Data
@Builder
public class ProgressDto {
    GoalDto goal;
    double progressPercentage;
}
