package com.example.plans.controller;

import com.example.plans.dto.AppResponseDto;
import com.example.plans.dto.PlanRequest;
import com.example.plans.dto.PlanResponse;
import com.example.plans.model.Plan;
import com.example.plans.service.PlanService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanControllerTest {

    @Mock
    private PlanService planService;

    @InjectMocks
    private PlanController planController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePlan() {
        PlanRequest request = new PlanRequest(); // populate as needed
        PlanResponse response = new PlanResponse(); // populate as needed

        when(planService.createPlan(request)).thenReturn(response);

        ResponseEntity<AppResponseDto> result = planController.createPlan(request);

        assertEquals(201, result.getStatusCodeValue());
        assertEquals("Plan created successfully", result.getBody().getMessage());
        verify(planService, times(1)).createPlan(request);
    }

    @Test
    void testGetPlanById() {
        String planId = "123";
        Plan plan = new Plan(); // populate as needed

        when(planService.getPlanById(planId)).thenReturn(Optional.of(plan));

        ResponseEntity<AppResponseDto> result = planController.getPlanById(planId);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Plan Name", result.getBody().getMessage());
        assertTrue(result.getBody().getData() instanceof Optional);
        verify(planService).getPlanById(planId);
    }

    @Test
    void testGetPlansByData() {
        String data = "test";
        int pageNumber = 0;
        int pageSize = 10;
        List<Plan> plans = Arrays.asList(new Plan(), new Plan());

        when(planService.getPlansByData(data, pageNumber, pageSize)).thenReturn(plans);

        List<Plan> result = planController.getPlansByData(data, pageNumber, pageSize);

        assertEquals(2, result.size());
        verify(planService).getPlansByData(data, pageNumber, pageSize);
    }

    @Test
    void testGetAllPlans() {
        List<Plan> plans = Arrays.asList(new Plan(), new Plan());

        when(planService.getAllPlans()).thenReturn(plans);

        ResponseEntity<List<Plan>> result = planController.getAllPlans();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().size());
        verify(planService).getAllPlans();
    }
}
