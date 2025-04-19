package org.mozgotrash.rest.response;

import lombok.Builder;
import lombok.Data;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;

import java.util.Base64;

@Data
@Builder
public class BookDto {
    Long id;

    String author;

    String title;

    Integer pageCount;

    String base64Image;

    Double percentageOfGoal;

    Double percentRead;

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

    private static double getPercentageOfGoal(Book book) {
        int goalPageSum = book.getGoal().getBooks().stream().mapToInt(Book::getPageCount).sum();
        return ((double) book.getPageCount() / goalPageSum ) * 100;
    }
}
