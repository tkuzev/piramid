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

    }
}
