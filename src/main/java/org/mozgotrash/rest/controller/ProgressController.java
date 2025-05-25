package org.mozgotrash.rest.controller;

import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.GoalRepository;
import org.mozgotrash.repository.LogRepository;
import org.mozgotrash.rest.request.AddBookRequest;
import org.mozgotrash.rest.response.GoalDto;
import org.mozgotrash.rest.response.ProgressDto;
import org.mozgotrash.service.impl.ProgressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressServiceImpl progressService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private LogRepository logRepository;


    //TODO вызыв вставленой в sql init цели
    @GetMapping("/current")
    ResponseEntity<ProgressDto> getProgress() {
        List<Goal> userGoals = goalRepository.findByUserId(1L);
        BigDecimal goalPercent = progressService.getGoalProgressPercentage(userGoals.get(0));
        GoalDto goalDto = GoalDto.fromEntity(userGoals.get(0));
        goalDto.getBooks()
                .forEach(bookDto -> {
                    BigDecimal percentRead = progressService.getBookProgressPercentage(bookDto.getId());
                    bookDto.setPercentRead(percentRead);
                });
        return ResponseEntity.ok(ProgressDto
                .builder()
                .goal(goalDto)
                .progressPercentage(goalPercent)
                .build());
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
