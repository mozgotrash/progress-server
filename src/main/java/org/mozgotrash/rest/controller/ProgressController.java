package org.mozgotrash.rest.controller;

import jakarta.websocket.server.PathParam;
import org.mozgotrash.model.Book;
import org.mozgotrash.model.Goal;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.GoalRepository;
import org.mozgotrash.repository.LogRepository;
import org.mozgotrash.rest.request.AddBookRequest;
import org.mozgotrash.rest.response.GoalDto;
import org.mozgotrash.rest.response.ProgressDto;
import org.mozgotrash.service.ProgressCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.util.Tuple;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ProgressCalculator progressCalculator;

    //TODO вызыв вставленой в sql init цели
    @GetMapping("/current")
    ResponseEntity<ProgressDto> getProgress() {
        Goal goal = goalRepository.findById(1l).get();
        double progressPercentage = progressCalculator.getProgressForGoal(goal);
        GoalDto goalDto = GoalDto.fromEntity(goal);
        goalDto.getBooks()
                .forEach(bookDto -> {
                    Double percentRead = (logRepository.findAllByBookId(bookDto.getId())
                            .stream()
                            .mapToDouble(Log::getPageCount).sum() / bookDto.getPageCount())
                            * 100;
                    bookDto.setPercentRead(percentRead);
                });
        return ResponseEntity.ok(ProgressDto
                .builder()
                .goal(goalDto)
                .progressPercentage(progressPercentage)
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
