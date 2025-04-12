package org.mozgotrash.rest.request;

import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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

    Goal goal;

    public static BookDto fromEntity(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .title(book.getTitle())
                .pageCount(book.getPageCount())
                .base64Image(Base64.getEncoder().encodeToString(book.getImageData()))
                .build();
    }
}
