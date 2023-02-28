package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
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
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Test
    void registerNewPerson() {
        RegistrationPerson registrationPerson = registrationPersonService.registerPerson("Person", "email@person.com", new BigDecimal("500"), 1L);

        registrationPersonService.setSubscription(registrationPerson,4);
        binaryRegistrationService.registerNewPerson(registrationPerson, false);
        BinaryPerson binaryPerson = binaryPersonRepository.findByEmail("email@person.com").orElseThrow();

        assertEquals("email@person.com", binaryPerson.getEmail());
    }

    @Test
    void testBinaryPersonRegistration(){
        RegistrationPerson person2 = registrationPersonService.registerPerson("Person2", "2@com", new BigDecimal("250"), 1L);
        RegistrationPerson person3 = registrationPersonService.registerPerson("Person3", "3@com", new BigDecimal("500"), 1L);
        RegistrationPerson person4 = registrationPersonService.registerPerson("Person4", "4@com", new BigDecimal("500"), 1L);
        RegistrationPerson person5 = registrationPersonService.registerPerson("Person5", "5@com", new BigDecimal("250"), 3L);
        RegistrationPerson person6 = registrationPersonService.registerPerson("Person6", "6@com", new BigDecimal("500"), 5L);

        BinaryPerson binPerson3 = binaryRegistrationService.registerNewPerson(person3, false);
        BinaryPerson binPerson4 = binaryRegistrationService.registerNewPerson(person4, true);
        BinaryPerson binPerson6 = binaryRegistrationService.registerNewPerson(person6, true);


        assertFalse(binaryPersonRepository.findByEmail("2@com").isPresent());
        assertEquals(binPerson3.getId(), binaryPersonRepository.findById(1L).get().getRightChild().getId());
        assertEquals(binPerson4.getEmail(), binaryPersonRepository.findByEmail("3@com").get().getLeftChild().getEmail());
        assertFalse(binaryPersonRepository.findByEmail("5@com").isPresent());
        assertEquals(binPerson6.getEmail(), binaryPersonRepository.findByEmail("4@com").get().getRightChild().getEmail());
    }



    @Test  //ako she go testvash vzemi promeni crona ili izchakai da stane 1vi den ot meseca oligofren prost
    void scheduledMethod(){
        BinaryPerson boss = binaryPersonRepository.findById(1L).orElseThrow();
        boss.setLeftContainer(BigDecimal.valueOf(0));
        boss.setRightContainer(BigDecimal.valueOf(700));
        binaryPersonRepository.save(boss);
        RegistrationPerson person1 = registrationPersonService.registerPerson("Person3", "3@com", new BigDecimal("500"), 1L);
        RegistrationPerson person2 = registrationPersonService.registerPerson("Person4", "4@com", new BigDecimal("500"), 1L);
        RegistrationPerson person3 = registrationPersonService.registerPerson("Person6", "6@com", new BigDecimal("500"), 2L);

        BinaryPerson binPerson1 = binaryRegistrationService.registerNewPerson(person1, false);
        BinaryPerson binPerson2 = binaryRegistrationService.registerNewPerson(person2, true);
        BinaryPerson binPerson3 = binaryRegistrationService.registerNewPerson(person3, true);

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