package org.mozgotrash.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProgressController {

    @GetMapping("/progress")
    ResponseEntity<Double> getProgress() {
        return ResponseEntity.ok().body(37.4);
    }
}
