package org.mozgotrash.rest.response;

import lombok.Builder;
import lombok.Data;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;

@Data
@Builder
public class BookDto {
    Long id;

    String author;

    String title;

    Integer pageCount;

    String base64Image;

    BigDecimal percentageOfGoal;

    BigDecimal percentRead;

    Book.Status status;

    public static BookDto fromEntity(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .title(book.getTitle())
                .pageCount(book.getPageCount())
                .base64Image(Base64.getEncoder().encodeToString(book.getImageData()))
                .percentageOfGoal(getPercentageOfGoal(book))
                .status(book.getStatus())
                .build();
    }

    private static BigDecimal getPercentageOfGoal(Book book) {
        int total = book.getGoal().getBooks().stream().mapToInt(Book::getPageCount).sum();
        return BigDecimal.valueOf((book.getPageCount() / total) * 100L)
                .setScale(3, RoundingMode.FLOOR);
    }
}
