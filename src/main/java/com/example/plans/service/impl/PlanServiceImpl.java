package com.example.plans.service.impl;

import com.example.plans.dto.PlanRequest;
import com.example.plans.dto.PlanResponse;
import com.example.plans.model.Plan;
import com.example.plans.repository.PlanRepository;
import com.example.plans.service.PlanService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class PlanServiceImpl implements PlanService {
	
	private static final Logger logger =
    		LoggerFactory.getLogger(PlanServiceImpl.class);

	@Autowired private PlanRepository repository;
	
    @Override
    public PlanResponse createPlan(PlanRequest planRequest) {
    	logger.warn("plan cannot be null");
    	if (planRequest == null) throw new IllegalArgumentException("plan cannot be null");
		if (!StringUtils.hasText(planRequest.getName()) || planRequest.getName().length() > 100) {
			throw new IllegalArgumentException("Name is mandatory and must be at most 100 characters");
		}
		if (!"prepaid".equalsIgnoreCase(planRequest.getType()) && !"postpaid".equalsIgnoreCase(planRequest.getType())) {
			throw new IllegalArgumentException("Type must be either 'prepaid' or 'postpaid'");
		}
		if (!StringUtils.hasText(planRequest.getData()) || planRequest.getData().length() > 50) {
			throw new IllegalArgumentException("Data is mandatory and must be at most 50 characters");
		}
		if (planRequest.getAmount() == null || planRequest.getAmount() <= 0) {
			throw new IllegalArgumentException("Amount must be greater than 0");
		}
		if (planRequest.getValidity() < 1 || planRequest.getValidity() > 365) {
			throw new IllegalArgumentException("Validity must be between 1 and 365");
		}

        try {
        	Plan plan=new Plan();
        	BeanUtils.copyProperties(planRequest, plan);
        	Plan savedPlan=repository.save(plan);
        	
        	PlanResponse planResponse=new PlanResponse();
        	BeanUtils.copyProperties(savedPlan, planResponse);
            return planResponse;
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("Plan already exists");
        }
    }
    
    @Override
    public Optional<Plan> getPlanById(String id) {
    	logger.error("plan not found");
        return repository.findById(id);
    }
    
	@Override
	public List<Plan> getPlansByData(String data, int pageNumber, int pageSize) {
		Pageable page=PageRequest.of(pageNumber, pageSize);
		List<Plan> plans= repository.findByDataContains(data, page);
		return plans;
	}
	
	@Override
    public List<Plan> getAllPlans() {
        return repository.findAll();
    }

}
