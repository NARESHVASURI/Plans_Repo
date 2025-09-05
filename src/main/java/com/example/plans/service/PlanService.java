package com.example.plans.service;

import com.example.plans.dto.PlanRequest;
import com.example.plans.dto.PlanResponse;
import com.example.plans.model.Plan;
import java.util.List;
import java.util.Optional;

public interface PlanService {
    PlanResponse createPlan(PlanRequest planRequest);
    Optional<Plan> getPlanById(String id);
	List<Plan> getPlansByData(String data, int pageNumber, int pageSize);
	List<Plan> getAllPlans();
}
