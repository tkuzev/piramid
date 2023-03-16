//package com.example.piramidadjii.registrationTreeModule.services.impl;
//
//import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
//import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
//import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
//import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
//import com.example.piramidadjii.registrationTreeModule.services.RegistrationPersonService;
//import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class RegistrationPersonServiceImplTest {
//    @Autowired
//    private RegistrationPersonService registrationPersonService;
//
//    @Autowired
//    private RegistrationPersonRepository registrationPersonRepository;
//
//    @Autowired
//    private SubscriptionPlanRepository subscriptionPlanRepository;
//    @Autowired
//    private BankRepository bankRepository;
//    @Autowired
//    private SubscriptionPlanService subscriptionPlanService;
//
//    @Test()
//    void testRegistrationFail(){
//        assertThrows(RuntimeException.class, ()->{
//            RegistrationPerson person = registrationPersonService.registerPerson("Nqma me", "asda",new BigDecimal("100"), 1L);
//        });
//    }
//
//    @Test
//    void registerTestFourthTier() {
//        RegistrationPerson person = registrationPersonService.registerPerson("Person", "teodorkuzew@gmail.com",new BigDecimal("500"), 1L);
//        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();
//
//        //subscription plan
//        assertEquals(4L, personFromRepo.getSubscriptionPlan().getId());
//
//        //check money
//        assertEquals(BigDecimal.valueOf(0).setScale(2), personFromRepo.getBankAccount().getBalance());
//    }
//
//    @Test
//    void registerTestThirdTier() {
//        RegistrationPerson person = registrationPersonService.registerPerson("Person", "asdas",new BigDecimal("450"), 1L);
//        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();
//
//        //subscription plan
//        assertEquals(3L, personFromRepo.getSubscriptionPlan().getId());
//
//        //check money
//        assertEquals(BigDecimal.valueOf(50).setScale(2), personFromRepo.getBankAccount().getBalance());
//    }
//
//    @Test
//    void registerTestSecondTier() {
//        RegistrationPerson person = registrationPersonService.registerPerson("Person", "asdasda",new BigDecimal("350"), 1L);
//        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();
//
//        //subscription plan
//        assertEquals(2L, personFromRepo.getSubscriptionPlan().getId());
//
//        //check money
//        assertEquals(BigDecimal.valueOf(50).setScale(2), personFromRepo.getBankAccount().getBalance());
//
//    }
//
//    @Test
//    void testEmailSender() {
//        RegistrationPerson person = registrationPersonService.registerPerson("Person", "kocaa.dd@abv.bg",new BigDecimal("5000"), 1L);
////        RegistrationPerson vladi = registrationPersonService.registerPerson("Person", "vladined01@gmail.com",new BigDecimal("200"), 1L);
//        //RegistrationPerson koce = registrationPersonService.registerPerson("Person", "kocaa.dd@abv.bg",new BigDecimal("200"), 1L);
//        person.setSubscriptionExpirationDate(LocalDate.now());
////        vladi.setSubscriptionExpirationDate(LocalDate.now());
//        //koce.setSubscriptionExpirationDate(LocalDate.now());
//        registrationPersonRepository.save(person);
////        registrationPersonRepository.save(vladi);
//        //registrationPersonRepository.save(koce);
//
//    }
//
//    @Test
//    void registerTestFirstTier() {
//        RegistrationPerson person = registrationPersonService.registerPerson("Person","asdasda" ,new BigDecimal("250"), 1L);
//        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();
//
//        //subscription plan
//        assertEquals(1L, personFromRepo.getSubscriptionPlan().getId());
//
//        //check money
//        assertEquals(BigDecimal.valueOf(50).setScale(2), personFromRepo.getBankAccount().getBalance());
//    }
//
//    @Test
//    void upgradeSubscriptionPlanTestSuccessfully(){
//        RegistrationPerson person = registrationPersonService.registerPerson("Person","asdasdas" ,new BigDecimal("250"), 1L);
//        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();
//
//        personFromRepo.getBankAccount().setBalance(personFromRepo.getBankAccount().getBalance().add(BigDecimal.valueOf(500)));
//        subscriptionPlanService.upgradeSubscriptionPlan(personFromRepo, subscriptionPlanRepository.getSubscriptionPlanById(3L).orElseThrow());
//        registrationPersonRepository.save(personFromRepo);
//
//        assertEquals(3L, personFromRepo.getSubscriptionPlan().getId());
//        assertEquals(BigDecimal.valueOf(350).setScale(2), personFromRepo.getBankAccount().getBalance());
//    }
//
//    @Test
//    void upgradeSubscriptionPlanTestUnsuccessfully(){
//        RegistrationPerson person = registrationPersonService.registerPerson("Person", "asdasdas",new BigDecimal("250"), 1L);
//        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();
//
//        subscriptionPlanService.upgradeSubscriptionPlan(personFromRepo, subscriptionPlanRepository.getSubscriptionPlanById(3L).orElseThrow());
//
//        assertEquals(1L, personFromRepo.getSubscriptionPlan().getId());
//        assertEquals(BigDecimal.valueOf(50).setScale(2), personFromRepo.getBankAccount().getBalance());
//    }
//
//    @Test
//    void testParentAndBankAccount() {
//        RegistrationPerson person = registrationPersonService.registerPerson("Person", "asdasda",new BigDecimal("250"), 1L);
//        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();
//
//        //check bank account id = person id
//        assertEquals(person.getId(), personFromRepo.getBankAccount().getId());
//
//        //check parent id
//        assertEquals(1L, personFromRepo.getParent().getId());
//    }
//
//    @Test
//    void testRegistrationFeeTransactions() {
//        int oldTransactions = bankRepository.findAll().size();
//
//        RegistrationPerson person = registrationPersonService.registerPerson("Person", "dasdasda",new BigDecimal("250"), 1L);
//        RegistrationPerson personFromRepo = registrationPersonRepository.findById(person.getId()).get();
//
//        assertEquals(oldTransactions + 2, bankRepository.findAll().size());
//    }
//
//    @Test
//    void testRegistrationTreeCreatedCorrectly() {
//        RegistrationPerson person2 = registrationPersonService.registerPerson("111","kdkasdka", new BigDecimal("250"), 1L);
//        RegistrationPerson person3 = registrationPersonService.registerPerson("222","kdkasdka", new BigDecimal("250"), 1L);
//        RegistrationPerson person4 = registrationPersonService.registerPerson("333","kdkasdka", new BigDecimal("250"), 2L);
//        RegistrationPerson person5 = registrationPersonService.registerPerson("444","kdkasdka", new BigDecimal("250"), 2L);
//        RegistrationPerson person6 = registrationPersonService.registerPerson("555","kdkasdka", new BigDecimal("250"), 3L);
//
//        assertEquals(1L, registrationPersonRepository.findById(person2.getId()).get().getParent().getId());
//        assertEquals(1L, registrationPersonRepository.findById(person3.getId()).get().getParent().getId());
//        assertEquals(2L, registrationPersonRepository.findById(person4.getId()).get().getParent().getId());
//        assertEquals(2L, registrationPersonRepository.findById(person5.getId()).get().getParent().getId());
//        assertEquals(3L, registrationPersonRepository.findById(person6.getId()).get().getParent().getId());
//    }
//
//}
//
