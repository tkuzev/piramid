package com.example.piramidadjii.testFacade;

import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
public class FacadeServiceImplTest {
    @Autowired
    FacadeService facadeService;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    RegistrationPersonRepository registrationPersonRepository;

    @Test
    void testkurami(){
        facadeService.registerPerson("asd","kuramiemail",
                BigDecimal.valueOf(500),1L,subscriptionPlanRepository.getSubscriptionPlanById(2L).orElseThrow());

        RegistrationPerson kur = registrationPersonRepository.findById(2L).orElseThrow();

        facadeService.createTransaction(kur,BigDecimal.valueOf(1000));
        facadeService.createTransaction(kur,BigDecimal.valueOf(1000));
        facadeService.createTransaction(kur,BigDecimal.valueOf(1000));
        facadeService.createTransaction(kur,BigDecimal.valueOf(10000));
        facadeService.createTransaction(kur,BigDecimal.valueOf(1000));
    }
}
