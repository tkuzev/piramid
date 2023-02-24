package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTreeRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;

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

    @Test
    void registerNewPerson() {
        registrationTreeService.registerPerson("Person", "email@person.com", new BigDecimal("500"), 1L);

        RegistrationTree registrationTree = registrationTreeRepository.getFirstByEmail("email@person.com").orElseThrow();
        registrationTreeService.setSubscription(registrationTree,4);
        binaryRegistrationService.registerNewPerson(registrationTree, false);
        BinaryTree binaryTree = binaryTreeRepository.findByEmail("email@person.com").orElseThrow();

        assertEquals("email@person.com", binaryTree.getEmail());
    }

    @Test
    void testBinaryPersonRegistration(){
        RegistrationTree person2 = registrationTreeService.registerPerson("Person2", "2.com", new BigDecimal("250"), 1L);
        RegistrationTree person3 = registrationTreeService.registerPerson("Person3", "3.com", new BigDecimal("500"), 1L);
        RegistrationTree person4 = registrationTreeService.registerPerson("Person4", "4.com", new BigDecimal("500"), 1L);
        RegistrationTree person5 = registrationTreeService.registerPerson("Person5", "5.com", new BigDecimal("250"), 3L);
        RegistrationTree person6 = registrationTreeService.registerPerson("Person6", "6.com", new BigDecimal("500"), 5L);

        BinaryTree binPerson3 = binaryRegistrationService.registerNewPerson(person3, false);
        BinaryTree binPerson4 = binaryRegistrationService.registerNewPerson(person4, true);
        BinaryTree binPerson6 = binaryRegistrationService.registerNewPerson(person6, true);


        assertFalse(binaryTreeRepository.findByEmail("2.com").isPresent());
        assertEquals(binPerson3.getId(), binaryTreeRepository.findById(1L).get().getRightChild().getId());
        assertEquals(binPerson4.getEmail(), binaryTreeRepository.findByEmail("3.com").get().getLeftChild().getEmail());
        assertFalse(binaryTreeRepository.findByEmail("5.com").isPresent());
        assertEquals(binPerson6.getEmail(), binaryTreeRepository.findByEmail("4.com").get().getRightChild().getEmail());
    }



    @Test  //ako she go testvash vzemi promeni crona ili izchakai da stane 1vi den ot meseca oligofren prost
    void scheduledMethod(){
        RegistrationTree person1 = registrationTreeService.registerPerson("Person3", "3.com", new BigDecimal("500"), 1L);
        RegistrationTree person2 = registrationTreeService.registerPerson("Person4", "4.com", new BigDecimal("500"), 1L);
        RegistrationTree person3 = registrationTreeService.registerPerson("Person6", "6.com", new BigDecimal("500"), 2L);

        BinaryTree binPerson1 = binaryRegistrationService.registerNewPerson(person1, false);
        BinaryTree binPerson2 = binaryRegistrationService.registerNewPerson(person2, true);
        BinaryTree binPerson3 = binaryRegistrationService.registerNewPerson(person3, true);

        binPerson1.setBalance(BigDecimal.ZERO);
        binPerson1.setLeftContainer(BigDecimal.valueOf(1000L));
        binPerson1.setRightContainer(BigDecimal.valueOf(100L));

        binPerson2.setBalance(BigDecimal.ZERO);
        binPerson2.setLeftContainer(BigDecimal.valueOf(500L));
        binPerson2.setRightContainer(BigDecimal.valueOf(1000L));

        binPerson3.setBalance(BigDecimal.ZERO);
        binPerson3.setLeftContainer(BigDecimal.valueOf(100000L));
        binPerson3.setRightContainer(BigDecimal.valueOf(8000L));

        binaryTreeRepository.save(binPerson1);
        binaryTreeRepository.save(binPerson2);
        binaryTreeRepository.save(binPerson3);
    }
}