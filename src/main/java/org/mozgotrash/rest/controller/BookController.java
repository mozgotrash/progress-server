package org.mozgotrash.rest.controller;

import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.rest.response.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/{id}")
    ResponseEntity<BookDto> getBook(@PathVariable("id") Long id) {
        return bookRepository.findById(id)
                .map(b -> ResponseEntity.ok().body(BookDto.fromEntity(b)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
