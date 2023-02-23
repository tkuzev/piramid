package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTreeRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class BinaryRegistrationServiceImplTest {

    @Autowired
    private BinaryTreeRepository binaryTreeRepository;
    @Autowired
    private RegistrationTreeService registrationTreeService;
    @Autowired
    private BinaryRegistrationService binaryRegistrationService;
    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Test
    void registerNewPerson() {
        registrationTreeService.registerPerson("Person", "email@person.com", new BigDecimal("500"), 1L);

        RegistrationTree registrationTree = registrationTreeRepository.getFirstByEmail("email@person.com").orElseThrow();
        registrationTreeService.setSubscription(registrationTree,4);
        binaryRegistrationService.registerNewPerson(registrationTree);
        BinaryTree binaryTree = binaryTreeRepository.findByEmail("email@person.com").orElseThrow();

        assertEquals("email@person.com", binaryTree.getEmail());
    }

    @Test
    void method(){
        registrationTreeService.registerPerson("Person2", "2.com", new BigDecimal("250"), 1L);
        registrationTreeService.registerPerson("Person3", "3.com", new BigDecimal("500"), 1L);
        registrationTreeService.registerPerson("Person4", "4.com", new BigDecimal("500"), 1L);
        registrationTreeService.registerPerson("Person5", "5.com", new BigDecimal("250"), 3L);
        registrationTreeService.registerPerson("Person6", "6.com", new BigDecimal("500"), 5L);

        BinaryTree binPerson3 = binaryTreeRepository.findByEmail("3.com").orElseThrow();
        BinaryTree binPerson4 = binaryTreeRepository.findByEmail("4.com").orElseThrow();
        BinaryTree binPerson6 = binaryTreeRepository.findByEmail("6.com").orElseThrow();

        binPerson3.setPreferredDirection(false);
        binPerson4.setPreferredDirection(true);
        binaryTreeRepository.save(binPerson3);
        binaryTreeRepository.save(binPerson4);

        assertFalse(binaryTreeRepository.findByEmail("2.com").isPresent());
        assertEquals(binPerson3.getId(), binaryTreeRepository.findById(1L).get().getRightChild().getId());
        assertEquals(binPerson4.getId(), binPerson3.getLeftChild().getId());
        assertFalse(binaryTreeRepository.findByEmail("4.com").isPresent());
        assertEquals(binPerson6.getId(), binPerson4.getRightChild().getId());
    }
}