package com.example.plans.service;

import com.example.plans.model.Plan;
import com.example.plans.repository.PlanRepository;
import com.example.plans.service.impl.PlanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanServiceTest {

    @Mock
    private PlanRepository repository;

    @InjectMocks
    private PlanServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlan_success() {
        Plan p = new Plan(null, "Basic", "prepaid", "1GB", BigDecimal.valueOf(99), 28);
        when(repository.save(any(Plan.class))).thenAnswer(i -> {
            Plan arg = i.getArgument(0);
            arg.setId("123");
            return arg;
        });
        Plan created = service.createPlan(p);
        assertNotNull(created.getId());
        verify(repository, times(1)).save(p);
    }

    @Test
    void getPlan_found() {
        Plan p = new Plan("id1", "Basic", "prepaid", "1GB", BigDecimal.valueOf(99), 28);
        when(repository.findById("id1")).thenReturn(Optional.of(p));
        Optional<Plan> found = service.getPlan("id1");
        assertTrue(found.isPresent());
        assertEquals("Basic", found.get().getName());
    }

    @Test
    void listPlans_page() {
        Plan p1 = new Plan("1","A","pre","1GB", BigDecimal.valueOf(10), 30);
        Page<Plan> page = new PageImpl<>(List.of(p1));
        when(repository.findAll(PageRequest.of(0,10))).thenReturn(page);
        Page<Plan> result = service.listPlans(PageRequest.of(0,10));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deletePlan_callsRepository() {
        doNothing().when(repository).deleteById("id1");
        service.deletePlan("id1");
        verify(repository, times(1)).deleteById("id1");
    }
}
