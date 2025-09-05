package com.example.plans.controller;

import com.example.plans.dto.AppResponseDto;
import com.example.plans.dto.PlanRequest;
import com.example.plans.dto.PlanResponse;
import com.example.plans.model.Plan;
import com.example.plans.service.PlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
	
	private static final Logger logger =
    		LoggerFactory.getLogger(PlanController.class);

	@Autowired private PlanService planService;

    @PostMapping("/create")
    public ResponseEntity<AppResponseDto> createPlan(@Valid @RequestBody PlanRequest planRequest) {
    	logger.warn("create the plan with name type data and vality");
        PlanResponse planResponse = planService.createPlan(planRequest);
        AppResponseDto appResponseDto=new AppResponseDto(2001, "Plan created successfully", planResponse);
        return new ResponseEntity<AppResponseDto>(appResponseDto, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AppResponseDto> getPlanById(@PathVariable String id) {
    	logger.error("Invalid plan provided");
    	Optional<Plan> plan=planService.getPlanById(id);
    	AppResponseDto appResponseDto=new AppResponseDto(2001, "Plan Name", plan);
        return new ResponseEntity<AppResponseDto>(appResponseDto, HttpStatus.OK);
    }
    
    @GetMapping("/getPlansByData")
	public List<Plan> getPlansByData(@RequestParam String data, int pageNumber,int pageSize) {
		return planService.getPlansByData(data, pageNumber, pageSize);
	} 
    
    @GetMapping("/getAll")
    public ResponseEntity<List<Plan>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }
}
