package com.example.piramidadjii.repositories;

import com.example.piramidadjii.entities.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<Plan,Long> {
    Optional<Plan> getPlanById(Long id);
}
