package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.binaryTreeModule.services.DistributeMoneyService;
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
    private BinaryRegistrationService binaryRegistrationService;
    @Autowired
    private DistributeMoneyService distributeMoneyService;
    @Test
    void testMoneyInContainersDistribution() {
        registrationPersonService.registerPerson("Person2", "kurkurkur",new BigDecimal("250"), 1L);
        RegistrationPerson person3 = registrationPersonService.registerPerson("Person3","kurlur" ,new BigDecimal("500"), 1L);
        RegistrationPerson person4 = registrationPersonService.registerPerson("Person4","putka" ,new BigDecimal("500"), 1L);
        registrationPersonService.registerPerson("Person5","pedal" ,new BigDecimal("250"), 3L);
        RegistrationPerson person6 = registrationPersonService.registerPerson("Person6","vagina" ,new BigDecimal("500"), 5L);

        binaryRegistrationService.registerNewBinaryPerson(person3, binaryPersonRepository.findById(1L).orElseThrow(), true);
        binaryRegistrationService.registerNewBinaryPerson(person4, binaryPersonRepository.findById(3L).orElseThrow(), false);
        binaryRegistrationService.registerNewBinaryPerson(person6, binaryPersonRepository.findById(4L).orElseThrow(), true);

        binaryPersonRepository.findById(3L).orElseThrow();
        BinaryPerson binPerson4 = binaryPersonRepository.findById(4L).orElseThrow();
        BinaryPerson binPerson6 = binaryPersonRepository.findById(6L).orElseThrow();

        binPerson4.setLeftContainer(BigDecimal.valueOf(400L));
        binaryPersonRepository.save(binPerson4);

        distributeMoneyService.distributeMoney(binPerson6, BigDecimal.valueOf(300));

        //person6
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findById(person6.getId()).get().getLeftContainer());
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findById(person6.getId()).get().getRightContainer());

        //person4
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findById(person4.getId()).get().getLeftContainer());
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryPersonRepository.findById(person4.getId()).get().getRightContainer());

        //person3
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryPersonRepository.findById(person3.getId()).get().getLeftContainer());
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findById(person3.getId()).get().getRightContainer());

        //Boss
        assertEquals(BigDecimal.ZERO.setScale(2), binaryPersonRepository.findById(1L).get().getLeftContainer());
        assertEquals(BigDecimal.valueOf(300).setScale(2), binaryPersonRepository.findById(1L).get().getRightContainer());
    }
}