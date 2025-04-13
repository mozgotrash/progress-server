package org.mozgotrash.rest.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class GoalDto {
    Long id;

    String title;

    List<BookDto> books;

    LocalDate deadline;

    public static GoalDto fromEntity(Goal goal) {
        return GoalDto.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .books(goal.getBooks().stream().map(BookDto::fromEntity).collect(Collectors.toList()))
                .deadline(goal.getDeadline())
                .build();
    }
}
