package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTreeRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.binaryTreeModule.services.DistributeMoneyService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DistributeMoneyServiceImplTest {
    @Autowired
    private BinaryTreeRepository binaryTreeRepository;
    @Autowired
    private RegistrationTreeService registrationTreeService;
    @Autowired
    private BinaryRegistrationService binaryRegistrationService;
    @Autowired
    private DistributeMoneyService distributeMoneyService;
    @Test
    void distributeMoneyWithoutBossMoney() {
        RegistrationTree person2 = registrationTreeService.registerPerson("Person2", "2.com", new BigDecimal("250"), 1L);
        RegistrationTree person3 = registrationTreeService.registerPerson("Person3", "3.com", new BigDecimal("500"), 1L);
        RegistrationTree person4 = registrationTreeService.registerPerson("Person4", "4.com", new BigDecimal("500"), 1L);
        RegistrationTree person5 = registrationTreeService.registerPerson("Person5", "5.com", new BigDecimal("250"), 3L);
        RegistrationTree person6 = registrationTreeService.registerPerson("Person6", "6.com", new BigDecimal("500"), 5L);

        BinaryTree binPerson3 = binaryRegistrationService.registerNewPerson(person3, false);
        BinaryTree binPerson4 = binaryRegistrationService.registerNewPerson(person4, true);
        BinaryTree binPerson6 = binaryRegistrationService.registerNewPerson(person6, true);

        distributeMoneyService.distributeMoney(binPerson6, BigDecimal.valueOf(300));

        //person6
        assertEquals(BigDecimal.ZERO.setScale(2), binaryTreeRepository.findByEmail("6.com").get().getLeftContainer());
        assertEquals(BigDecimal.ZERO.setScale(2), binaryTreeRepository.findByEmail("6.com").get().getRightContainer());

        //person4
        assertEquals(BigDecimal.ZERO.setScale(2), binaryTreeRepository.findByEmail("4.com").get().getLeftContainer());
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryTreeRepository.findByEmail("4.com").get().getRightContainer());

        //person3
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryTreeRepository.findByEmail("3.com").get().getLeftContainer());
        assertEquals(BigDecimal.ZERO.setScale(2), binaryTreeRepository.findByEmail("3.com").get().getRightContainer());

        //Boss
        assertEquals(BigDecimal.ZERO.setScale(2), binaryTreeRepository.findById(1L).get().getLeftContainer());
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryTreeRepository.findById(1L).get().getRightContainer());
    }
}