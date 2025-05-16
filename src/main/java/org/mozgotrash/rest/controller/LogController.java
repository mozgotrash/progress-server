package org.mozgotrash.rest.controller;

import org.mozgotrash.model.Book;
import org.mozgotrash.model.Log;
import org.mozgotrash.repository.BookRepository;
import org.mozgotrash.repository.LogRepository;
import org.mozgotrash.rest.request.LogRequestDto;
import org.mozgotrash.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private ProgressService progressService;

    @PostMapping
    ResponseEntity<Void> logCurrentBook(Integer logPage) {
        progressService.logProgress(logPage);
        return ResponseEntity.ok().build();
    }
}
