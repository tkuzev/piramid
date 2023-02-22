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
        registrationTreeService.registerPerson("Person1", "email@person1.com", new BigDecimal("250"), 1L);
        registrationTreeService.registerPerson("Person2", "email@person2.com", new BigDecimal("500"), 1L); // STACKOVERFLOW EXCEPTION HAHAHAH BEZKRAINA REKURSIIKAAAAA MMMMMMMMMM
        registrationTreeService.registerPerson("Person3", "email@person3.com", new BigDecimal("500"), 1L);
        registrationTreeService.registerPerson("Person4", "email@person4.com", new BigDecimal("500"), 3L); // gurmi samo s parent id 2 S DRUGITE NE???????????

        BinaryTree binPerson2 = binaryTreeRepository.findByEmail("email@person2.com").orElseThrow();
        BinaryTree binPerson3 = binaryTreeRepository.findByEmail("email@person3.com").orElseThrow();
        BinaryTree binPerson4 = binaryTreeRepository.findByEmail("email@person4.com").orElseThrow();

        binPerson2.setPreferredDirection(false);
        binPerson3.setPreferredDirection(true);
        binaryTreeRepository.save(binPerson2);
        binaryTreeRepository.save(binPerson3);

        assertEquals(binPerson2, binaryTreeRepository.findById(1L).get().getRightChild());
        assertEquals(binPerson3, binPerson2.getLeftChild());
        assertEquals(binPerson4, binPerson3.getRightChild());

        assertFalse(binaryTreeRepository.findByEmail("email@person1.com").isPresent());
    }
}