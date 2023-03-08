package com.example.piramidadjii.baseModule;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class BaseInit implements CommandLineRunner {

    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public void run(String... args) throws Exception {
        if (subscriptionPlanRepository.count()==0){
            SubscriptionPlan bronze=new SubscriptionPlan();
            bronze.setId(1L);
            bronze.setName("Bronze");
            bronze.setPercents("2");
            bronze.setEligibleForBinary(false);
            bronze.setRegistrationFee(BigDecimal.valueOf(200L));
            subscriptionPlanRepository.save(bronze);

            SubscriptionPlan silver=new SubscriptionPlan();
            silver.setId(2L);
            silver.setName("Silver");
            silver.setPercents("3//2");
            silver.setEligibleForBinary(false);
            silver.setRegistrationFee(BigDecimal.valueOf(300L));
            subscriptionPlanRepository.save(silver);

            SubscriptionPlan gold=new SubscriptionPlan();
            gold.setId(3L);
            gold.setName("Gold");
            gold.setPercents("4//3//2");
            gold.setEligibleForBinary(true);
            gold.setRegistrationFee(BigDecimal.valueOf(400L));
            subscriptionPlanRepository.save(gold);

            SubscriptionPlan platinum=new SubscriptionPlan();
            platinum.setId(4L);
            platinum.setName("Platinum");
            platinum.setPercents("5//4//3//2");
            platinum.setEligibleForBinary(true);
            platinum.setRegistrationFee(BigDecimal.valueOf(500L));
            subscriptionPlanRepository.save(platinum);
        }
        if (bankAccountRepository.count()==0){
            BankAccount bossBank=new BankAccount();
            BankAccount helperBank=new BankAccount();
            bossBank.setBalance(BigDecimal.ZERO);
            bossBank.setId(1L);
            helperBank.setId(-1L);
            helperBank.setBalance(BigDecimal.ZERO);

            bankAccountRepository.save(bossBank);
            bankAccountRepository.save(helperBank);
        }
        if (registrationPersonRepository.count()==0){
            RegistrationPerson boss=new RegistrationPerson();
            boss.setName("Boss");
            boss.setId(1L);
            boss.setBankAccount(bankAccountRepository.getReferenceById(1L));

            registrationPersonRepository.save(boss);
        }
        if (binaryPersonRepository.count()==0){
            BinaryPerson boss=new BinaryPerson();
            boss.setId(1L);
            boss.setLeftContainer(BigDecimal.ZERO);
            boss.setRightContainer(BigDecimal.ZERO);
            boss.setName("Boss");
            boss.setPreferredDirection(true);
            boss.setBankAccount(bankAccountRepository.getReferenceById(1L));

            binaryPersonRepository.save(boss);
        }

    }

}
