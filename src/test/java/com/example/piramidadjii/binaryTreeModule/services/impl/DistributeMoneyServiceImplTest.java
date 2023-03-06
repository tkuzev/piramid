package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.DistributeMoneyService;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DistributeMoneyServiceImplTest {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;
    @Autowired
    private RegistrationPersonService registrationPersonService;
    @Autowired
    private OrchestraService orchestraService;
    @Autowired
    private DistributeMoneyService distributeMoneyService;
    @Test
    void distributeMoneyWithoutBossMoney() {
        RegistrationPerson person2 = registrationPersonService.registerPerson("Person2", "2.com", new BigDecimal("250"), 1L);
        RegistrationPerson person3 = registrationPersonService.registerPerson("Person3", "3.com", new BigDecimal("500"), 1L);
        RegistrationPerson person4 = registrationPersonService.registerPerson("Person4", "4.com", new BigDecimal("500"), 1L);
        RegistrationPerson person5 = registrationPersonService.registerPerson("Person5", "5.com", new BigDecimal("250"), 3L);
        RegistrationPerson person6 = registrationPersonService.registerPerson("Person6", "6.com", new BigDecimal("500"), 5L);

        BinaryPerson binPerson3 = orchestraService.registerNewPerson(person3, false);
        BinaryPerson binPerson4 = orchestraService.registerNewPerson(person4, true);
        BinaryPerson binPerson6 = orchestraService.registerNewPerson(person6, true);
        binPerson4.setLeftContainer(BigDecimal.valueOf(400L));
        binaryPersonRepository.save(binPerson4);

        distributeMoneyService.distributeMoney(binPerson6, BigDecimal.valueOf(300));

        //person6
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findByEmail("6.com").get().getLeftContainer());
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findByEmail("6.com").get().getRightContainer());

        //person4
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findByEmail("4.com").get().getLeftContainer());
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryPersonRepository.findByEmail("4.com").get().getRightContainer());

        //person3
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryPersonRepository.findByEmail("3.com").get().getLeftContainer());
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findByEmail("3.com").get().getRightContainer());

        //Boss
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findById(1L).get().getLeftContainer());
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryPersonRepository.findById(1L).get().getRightContainer());
    }
}