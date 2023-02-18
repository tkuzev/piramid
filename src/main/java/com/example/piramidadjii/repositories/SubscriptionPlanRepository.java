package com.example.piramidadjii.repositories;

import com.example.piramidadjii.entities.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan,Long> {
    Optional<SubscriptionPlan> getSubscriptionPlanById(Long id);
}
