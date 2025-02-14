package com.bookstorage.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "book-tracker-service", url = "http://localhost:8081")
public interface BookTrackerClient {
    @PostMapping("/tracker")
    void createTracker(@RequestParam Long bookId);

    @DeleteMapping("/tracker/{bookId}")
    void deleteTracker(@PathVariable Long bookId);
}
