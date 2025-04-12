package org.mozgotrash.rest.controller;

import jakarta.websocket.server.PathParam;
import org.mozgotrash.model.Book;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.rest.request.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/current")
    ResponseEntity<Double> getProgress() {
        return ResponseEntity.ok().body(37.4);
    }

    @PostMapping("/book")
    ResponseEntity<Book> addBook(@RequestBody AddBookRequest addBookRequest) {
        Book book = Book.builder()
                .title(addBookRequest.getTitle())
                .author(addBookRequest.getAuthor())
                .pageCount(addBookRequest.getPageCount())
                .imageData(Base64.getDecoder().decode(addBookRequest.getImageBase64().split(",")[1]))
                .build();
        Book saved = bookRepository.save(book);
        return ResponseEntity.ok(saved);
    }
}
