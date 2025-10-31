package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.entity.ReviewCycle;
import com.quantumsoft.hrms.servicei.ReviewCycleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review-cycles")
public class ReviewCycleController {

    private final ReviewCycleService reviewCycleService;

    public ReviewCycleController(ReviewCycleService reviewCycleService) {
        this.reviewCycleService = reviewCycleService;
    }

    // 🔹 Create/Start new review cycle
    @PostMapping
    public ResponseEntity<ReviewCycle> create(@RequestBody ReviewCycle cycle) {
        return ResponseEntity.ok(reviewCycleService.createCycle(cycle));
    }

    // 🔹 Close a review cycle
    @PatchMapping("/{id}/close")
    public ResponseEntity<ReviewCycle> closeCycle(@PathVariable Long id) {
        return ResponseEntity.ok(reviewCycleService.closeCycle(id));
    }

    // 🔹 Get active cycles
    @GetMapping("/active")
    public ResponseEntity<List<ReviewCycle>> getActive() {
        return ResponseEntity.ok(reviewCycleService.getActiveCycles());
    }
}
