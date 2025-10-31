package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.entity.ReviewCycle;

import java.util.List;

public interface ReviewCycleService {
    ReviewCycle createCycle(ReviewCycle reviewCycle);
    ReviewCycle closeCycle(Long id);
    List<ReviewCycle> getActiveCycles();
}
