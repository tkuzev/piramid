package com.example.piramidadjii.initPeople;

import com.example.piramidadjii.entities.Person;
import com.example.piramidadjii.repositories.PersonRepository;
import com.example.piramidadjii.repositories.SubscriptionPlanRepository;
import com.example.piramidadjii.repositories.TransactionRepository;
import com.example.piramidadjii.services.impl.RegistrationTreeServiceImpl;
import com.example.piramidadjii.services.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Init implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    private final TransactionServiceImpl transactionService;

    private final RegistrationTreeServiceImpl registrationTreeService;

    public Init(TransactionServiceImpl transactionService, RegistrationTreeServiceImpl registrationTreeService) {
        this.transactionService = transactionService;
        this.registrationTreeService = registrationTreeService;
    }

    @Override
    public void run(String... args) throws Exception {
        Person person1 = new Person();
        person1.setId(2L);
        person1.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(1L).orElseThrow());
        person1.setName("1");
        person1.setBalance(BigDecimal.ZERO);
        person1.setParent(personRepository.getPersonById(1L).orElseThrow());
        personRepository.save(person1);

        Person person2 = new Person();
        person2.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(2L).orElseThrow());
        person2.setId(3L);
        person2.setName("2");
        person2.setBalance(BigDecimal.ZERO);
        person2.setParent(personRepository.getPersonById(1L).orElseThrow());
        personRepository.save(person2);

        Person person3 = new Person();
        person3.setId(4L);
        person3.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(1L).orElseThrow());
        person3.setName("3");
        person3.setBalance(BigDecimal.ZERO);
        person3.setParent(personRepository.getPersonById(2L).orElseThrow());
        personRepository.save(person3);

        Person person4 = new Person();
        person4.setId(5L);
        person4.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(1L).orElseThrow());
        person4.setName("4");
        person4.setBalance(BigDecimal.ZERO);
        person4.setParent(personRepository.getPersonById(3L).orElseThrow());
        personRepository.save(person4);

        Person person5 = new Person();
        person5.setId(6L);
        person5.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(1L).orElseThrow());
        person5.setName("5");
        person5.setBalance(BigDecimal.ZERO);
        person5.setParent(personRepository.getPersonById(4L).orElseThrow());
        personRepository.save(person5);

        Person person6 = new Person();
        person6.setId(7L);
        person6.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(1L).orElseThrow());
        person6.setName("person6");
        person6.setBalance(BigDecimal.ZERO);
        person6.setParent(personRepository.getPersonById(6L).orElseThrow());
        personRepository.save(person6);

        Person person7 = new Person();
        person7.setId(8L);
        person7.setSubscriptionPlan(subscriptionPlanRepository.getPlanById(1L).orElseThrow());
        person7.setName("person7");
        person7.setBalance(BigDecimal.ZERO);
        person7.setParent(personRepository.getPersonById(7L).orElseThrow());
        personRepository.save(person7);

        transactionService.createTransaction(person7, BigDecimal.valueOf(20L), 0);

        Person person8 = new Person();
        person8.setId(9L);
        person8.setName("person8");
        person8.setBalance(BigDecimal.valueOf(350));
        person8.setParent(personRepository.getPersonById(7L).orElseThrow());
        registrationTreeService.registerPerson(person8);

        personRepository.save(person8);

        Person person9 = new Person();
        person9.setId(10L);
        person9.setName("person9");
        person9.setBalance(BigDecimal.valueOf(1000));
        person9.setParent(personRepository.getPersonById(8L).orElseThrow());
        registrationTreeService.registerPerson(person9);

        personRepository.save(person9);
    }
}
