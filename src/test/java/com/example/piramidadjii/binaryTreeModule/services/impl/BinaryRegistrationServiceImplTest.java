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
        RegistrationTree person1 = createPerson(1L,1L,"asdasdasd@asd.bg");
        RegistrationTree person2 = createPerson(1L,3L, "asdasdasdasd@asd.bg");
        RegistrationTree person3 = createPerson(2L,3L,"asdasdasd@asd123.bg");
        RegistrationTree person4 = createPerson(3L,3L,"1231asdasdasd@asd.bg");

        binaryRegistrationService.registerNewPerson(person1);
        binaryRegistrationService.registerNewPerson(person2);
        binaryRegistrationService.registerNewPerson(person3);
        binaryRegistrationService.registerNewPerson(person4);

        BinaryTree binPerson1 = binaryTreeRepository.findByEmail("asdasdasd@asd.bg").orElseThrow();
        BinaryTree binPerson2 = binaryTreeRepository.findByEmail("asdasdasdasd@asd.bg").orElseThrow();
        BinaryTree binPerson3 = binaryTreeRepository.findByEmail("asdasdasd@asd123.bg").orElseThrow();
        BinaryTree binPerson4 = binaryTreeRepository.findByEmail("1231asdasdasd@asd.bg").orElseThrow();

        binPerson2.setPreferredDirection(false);
        binPerson3.setPreferredDirection(true);

        assertEquals(binPerson2, binaryTreeRepository.findById(1L).get().getRightChild());
        assertEquals(binPerson3, binPerson2.getLeftChild());
        assertEquals(binPerson4, binPerson3.getRightChild());

        assertEquals(false,binaryTreeRepository.findByEmail(binPerson1.getEmail()).isPresent());

    }

    private RegistrationTree createPerson(Long parentId, Long planId,String email) {
        RegistrationTree registrationTree = new RegistrationTree();
        registrationTree.setParent(registrationTreeRepository.getRegistrationTreeById(parentId).orElseThrow());
        registrationTree.setSubscriptionPlan(subscriptionPlanRepository.getSubscriptionPlanById(planId).orElseThrow());
        registrationTree.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
        registrationTree.setEmail(email);
        registrationTree = registrationTreeRepository.save(registrationTree);
        return registrationTree;
    }
}