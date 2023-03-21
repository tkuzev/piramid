package com.example.piramidadjii.facade.controller;

import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.facade.dto.BinaryPersonDTO;
import com.example.piramidadjii.facade.dto.DepositDTO;
import com.example.piramidadjii.facade.dto.RegisterPersonDTO;
import com.example.piramidadjii.facade.dto.UpgradeSubscriptionPlanDTO;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.services.SubscriptionPlanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;


@RestController
public class FacadeController {
    @Autowired
    private FacadeService facadeService;
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private BinaryRegistrationService binaryRegistrationService;

    @PostMapping("/register")
    public void registerPerson(@RequestBody RegisterPersonDTO registerPersonDTO){
        RegistrationPerson parent = registrationPersonRepository.findById(registerPersonDTO.getParentId()).orElseThrow();
        BigDecimal money=registerPersonDTO.getMoney();
        RegistrationPerson person = customModelMapper(registerPersonDTO, parent);
        facadeService.registerPerson(person,parent.getId(), money);
    }

    @PostMapping("/register/binary/{childId}")
    public void registerBinaryPerson(@PathVariable Long childId, @RequestBody BinaryPersonDTO binaryPersonDTO){
        binaryRegistrationService.registerNewBinaryPerson(binaryPersonDTO.getParent(),childId,binaryPersonDTO.getBinaryPersonToPutItOnId(),false);
    }

    @GetMapping("/income/{id}")
    public Map<SubscriptionPlan, BigDecimal> monthlyIncome(@PathVariable Long id){
        return facadeService.monthlyIncome(id);
    }

    @PostMapping("/user/deposit")
    public void deposit(@RequestBody DepositDTO depositDTO){
        facadeService.deposit(depositDTO.getId(),depositDTO.getMoney());
    }

    @PostMapping("/user/withdraw")
    public void withdraw(@RequestBody DepositDTO depositDTO){
        facadeService.withdraw(depositDTO.getId(),depositDTO.getMoney());
    }

    @PostMapping("/user/subscription-plan/upgrade")
    public void upgradeSubscriptionPlan(@RequestBody UpgradeSubscriptionPlanDTO upgradeSubscriptionPlanDTO){
        facadeService.
                upgradeSubscriptionPlan(upgradeSubscriptionPlanDTO.getRegistrationPerson(),
                        upgradeSubscriptionPlanDTO.getSubscriptionPlan());
    }


    private static RegistrationPerson customModelMapper(RegisterPersonDTO registerPersonDTO, RegistrationPerson parent) {
        RegistrationPerson person = new RegistrationPerson();
        person.setParent(parent);
        person.setName(registerPersonDTO.getName());
        person.setEmail(registerPersonDTO.getEmail());
        return person;
    }
}
