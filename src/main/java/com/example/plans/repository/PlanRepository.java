package com.example.plans.repository;

import com.example.plans.model.Plan;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends MongoRepository<Plan, String> {

	List<Plan> findByDataContains(String data, Pageable page);
}
