package com.codereviewbot.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.codereviewbot.model.PRReview;
import com.codereviewbot.repository.PRReviewRepository;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final PRReviewRepository prReviewRepository;

    public AnalyticsController(PRReviewRepository prReviewRepository) {
        this.prReviewRepository = prReviewRepository;
    }

    @GetMapping("/reviews")
    public List<PRReview> getAllReviews() {
        return prReviewRepository.findAll();
    }
}