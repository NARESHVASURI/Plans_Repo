package com.example.plans.controller;

import com.example.plans.dto.PlanRequest;
import com.example.plans.model.Plan;
import com.example.plans.service.PlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlanControllerTest {

    private MockMvc mockMvc;
    private PlanService planService;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        planService = Mockito.mock(PlanService.class);
        PlanController controller = new PlanController(planService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.example.plans.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void createPlan_valid() throws Exception {
        PlanRequest req = new PlanRequest();
        req.setName("Basic");
        req.setType("prepaid");
        req.setData("1GB");
        req.setAmount(BigDecimal.valueOf(99));
        req.setValidity(28);

        Plan created = new Plan("id1","Basic","prepaid","1GB", BigDecimal.valueOf(99),28);
        when(planService.createPlan(any(Plan.class))).thenReturn(created);

        mockMvc.perform(post("/api/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void createPlan_invalid_missingFields() throws Exception {
        PlanRequest req = new PlanRequest(); // missing all
        mockMvc.perform(post("/api/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void listPlans_pagination() throws Exception {
        Plan p = new Plan("1","A","pre","1GB", BigDecimal.valueOf(10), 30);
        Page<Plan> page = new PageImpl<>(List.of(p), PageRequest.of(0,10), 1);
        when(planService.listPlans(any())).thenReturn(page);

        mockMvc.perform(get("/api/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getPlan_notFound() throws Exception {
        when(planService.getPlan("nope")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/plans/nope"))
                .andExpect(status().isNotFound());
    }
}
