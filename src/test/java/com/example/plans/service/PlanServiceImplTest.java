package com.example.plans.service;

import com.example.plans.dto.PlanRequest;
import com.example.plans.dto.PlanResponse;
import com.example.plans.model.Plan;
import com.example.plans.repository.PlanRepository;
import com.example.plans.service.impl.PlanServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanServiceImplTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanServiceImpl planService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a valid PlanRequest
    private PlanRequest getValidPlanRequest() {
        PlanRequest request = new PlanRequest();
        request.setName("Unlimited Plan");
        request.setType("prepaid");
        request.setData("5GB/day");
        request.setAmount(299.0);
        request.setValidity(28);
        return request;
    }

    @Test
    void testCreatePlan_Success() {
        PlanRequest request = getValidPlanRequest();
        Plan savedPlan = new Plan();
        savedPlan.setName(request.getName());
        savedPlan.setType(request.getType());
        savedPlan.setData(request.getData());
        savedPlan.setAmount(request.getAmount());
        savedPlan.setValidity(request.getValidity());

        when(planRepository.save(any(Plan.class))).thenReturn(savedPlan);

        PlanResponse response = planService.createPlan(request);

        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    void testCreatePlan_InvalidName() {
        PlanRequest request = getValidPlanRequest();
        request.setName(""); // Invalid name

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            planService.createPlan(request);
        });

        assertEquals("Name is mandatory and must be at most 100 characters", exception.getMessage());
    }

    @Test
    void testCreatePlan_InvalidType() {
        PlanRequest request = getValidPlanRequest();
        request.setType("unknown");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            planService.createPlan(request);
        });

        assertEquals("Type must be either 'prepaid' or 'postpaid'", exception.getMessage());
    }

    @Test
    void testCreatePlan_DuplicateKeyException() {
        PlanRequest request = getValidPlanRequest();

        when(planRepository.save(any(Plan.class))).thenThrow(new DuplicateKeyException("Duplicate"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            planService.createPlan(request);
        });

        assertEquals("Plan already exists", exception.getMessage());
    }

    @Test
    void testGetPlanById_Found() {
        String planId = "123";
        Plan plan = new Plan();
        plan.setId(planId);

        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));

        Optional<Plan> result = planService.getPlanById(planId);

        assertTrue(result.isPresent());
        assertEquals(planId, result.get().getId());
        verify(planRepository, times(1)).findById(planId);
    }

    @Test
    void testGetPlanById_NotFound() {
        String planId = "999";

        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        Optional<Plan> result = planService.getPlanById(planId);

        assertFalse(result.isPresent());
        verify(planRepository, times(1)).findById(planId);
    }

    @Test
    void testGetPlansByData() {
        String data = "5GB";
        int pageNumber = 0;
        int pageSize = 2;
        List<Plan> plans = Arrays.asList(new Plan(), new Plan());

        when(planRepository.findByDataContains(eq(data), any(Pageable.class))).thenReturn(plans);

        List<Plan> result = planService.getPlansByData(data, pageNumber, pageSize);

        assertEquals(2, result.size());
        verify(planRepository, times(1)).findByDataContains(eq(data), any(Pageable.class));
    }

    @Test
    void testGetAllPlans() {
        List<Plan> plans = Arrays.asList(new Plan(), new Plan(), new Plan());

        when(planRepository.findAll()).thenReturn(plans);

        List<Plan> result = planService.getAllPlans();

        assertEquals(3, result.size());
        verify(planRepository, times(1)).findAll();
    }
}
