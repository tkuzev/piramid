package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class BinaryRegistrationServiceImplTest {

    @Autowired
    private BinaryPersonRepository binaryPersonRepository;
    @Autowired
    private RegistrationPersonService registrationPersonService;

    @Autowired
    private BinaryRegistrationService binaryRegistrationService;

    @Test
    void testRegisterNewPersonInBinarySuccessfully() {
        RegistrationPerson person = registrationPersonService.registerPerson("Person", "asdsadas",new BigDecimal("500"), 1L);
        binaryRegistrationService.registerNewBinaryPerson(person, false);
        binaryPersonRepository.findById(person.getId()).orElseThrow();

        assertTrue(binaryPersonRepository.findById(person.getId()).isPresent());
        assertEquals(1L, binaryPersonRepository.findById(person.getId()).get().getParent().getId());
    }

    @Test
    void testMultipleBinaryRegistrations(){
        RegistrationPerson person2 = registrationPersonService.registerPerson("Person2","teodorkuzew@gmail.com" ,new BigDecimal("3000"), 1L);
        RegistrationPerson person3 = registrationPersonService.registerPerson("Person3","babaminivan@gmail.com" , new BigDecimal("5000"), 1L);
        RegistrationPerson person4 = registrationPersonService.registerPerson("Person4","kocaa.dd@gmail.com" , new BigDecimal("4000"), 1L);
        RegistrationPerson person5 = registrationPersonService.registerPerson("Person5", "admin@unwe.bg" ,new BigDecimal("4000"), 3L);
        RegistrationPerson person6 = registrationPersonService.registerPerson("Person6", "vandonov@unwe.bg" ,new BigDecimal("3000"), 5L);

        BinaryPerson binPerson3 = binaryRegistrationService.registerNewBinaryPerson(person3, false);
        BinaryPerson binPerson4 = binaryRegistrationService.registerNewBinaryPerson(person4, true);
        BinaryPerson binPerson6 = binaryRegistrationService.registerNewBinaryPerson(person6, true);


        assertFalse(binaryPersonRepository.findById(person2.getId()).isPresent());
        assertEquals(binPerson3.getId(), binaryPersonRepository.findById(1L).get().getRightChild().getId());
        assertEquals(binPerson4.getId(), binaryPersonRepository.findById(person3.getId()).get().getLeftChild().getId());
        assertFalse(binaryPersonRepository.findById(person5.getId()).isPresent());
        assertEquals(binPerson6.getId(), binaryPersonRepository.findById(person4.getId()).get().getRightChild().getId());
    }

    @Test  //ako she go testvash vzemi promeni crona ili izchakai da stane 1vi den ot meseca oligofren prost
    void scheduledMethod(){
        BinaryPerson boss = binaryPersonRepository.findById(1L).orElseThrow();
        boss.setLeftContainer(BigDecimal.valueOf(0));
        boss.setRightContainer(BigDecimal.valueOf(700));
        binaryPersonRepository.save(boss);
        RegistrationPerson person1 = registrationPersonService.registerPerson("Person3", "asdasdas" ,new BigDecimal("500"), 1L);
        RegistrationPerson person2 = registrationPersonService.registerPerson("Person4", "asdsadas" ,new BigDecimal("500"), 1L);
        RegistrationPerson person3 = registrationPersonService.registerPerson("Person6", "sdasdasdsa" ,new BigDecimal("500"), 2L);

        BinaryPerson binPerson1 = binaryRegistrationService.registerNewBinaryPerson(person1, false);
        BinaryPerson binPerson2 = binaryRegistrationService.registerNewBinaryPerson(person2, true);
        BinaryPerson binPerson3 = binaryRegistrationService.registerNewBinaryPerson(person3, true);

        binPerson1.getBankAccount().setBalance(BigDecimal.ZERO);
        binPerson1.setLeftContainer(BigDecimal.valueOf(700));
        binPerson1.setRightContainer(BigDecimal.valueOf(0));

        binPerson2.getBankAccount().setBalance(BigDecimal.ZERO);
        binPerson2.setLeftContainer(BigDecimal.valueOf(400));
        binPerson2.setRightContainer(BigDecimal.valueOf(300));

        binPerson3.getBankAccount().setBalance(BigDecimal.ZERO);
        binPerson3.setLeftContainer(BigDecimal.valueOf(0));
        binPerson3.setRightContainer(BigDecimal.valueOf(0));

        binaryPersonRepository.save(binPerson1);
        binaryPersonRepository.save(binPerson2);
        binaryPersonRepository.save(binPerson3);
    }
}