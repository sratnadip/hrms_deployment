package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.entity.ReviewCycle;
import com.quantumsoft.hrms.enums.ReviewCycleStatus;
import com.quantumsoft.hrms.repository.ReviewCycleRepository;
import com.quantumsoft.hrms.servicei.ReviewCycleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewCycleServiceImpl implements ReviewCycleService {

    private final ReviewCycleRepository repository;

    public ReviewCycleServiceImpl(ReviewCycleRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReviewCycle createCycle(ReviewCycle reviewCycle) {
        reviewCycle.setStatus(String.valueOf(ReviewCycleStatus.ACTIVE));
        return repository.save(reviewCycle);
    }

    @Override
    public ReviewCycle closeCycle(Long id) {
        ReviewCycle cycle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cycle not found"));
        cycle.setStatus(String.valueOf(ReviewCycleStatus.CLOSED));
        return repository.save(cycle);
    }

    @Override
    public List<ReviewCycle> getActiveCycles() {
        return repository.findByStatus(String.valueOf(ReviewCycleStatus.ACTIVE));
    }
}
