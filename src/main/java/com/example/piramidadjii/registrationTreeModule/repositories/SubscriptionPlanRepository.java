package com.example.piramidadjii.registrationTreeModule.repositories;

import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Optional<SubscriptionPlan> getSubscriptionPlanById(Long id);
    Optional<SubscriptionPlan> getSubscriptionPlanByName(String name);
}
