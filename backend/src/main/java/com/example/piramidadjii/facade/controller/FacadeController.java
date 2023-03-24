package com.example.piramidadjii.facade.controller;

import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.facade.dto.*;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import jakarta.annotation.PostConstruct;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
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

    @PostMapping("/user/sell")
    public void makeSell(@RequestBody SellDTO sellDTO){

        RegistrationPerson person = registrationPersonRepository.findById(sellDTO.getId()).orElseThrow();
        facadeService.createTransaction(person,sellDTO.getPrice());
    }

    @PostMapping("/register")
    public void registerPerson(@RequestBody RegisterPersonDTO registerPersonDTO) {
        RegistrationPerson parent = registrationPersonRepository.findById(registerPersonDTO.getParentId()).orElseThrow();
        BigDecimal money = registerPersonDTO.getMoney();
        RegistrationPerson person = customModelMapper(registerPersonDTO, parent);
        facadeService.registerPerson(person, money);
    }

    @PostMapping("/register/binary/{childId}")
    public void registerBinaryPerson(@PathVariable Long childId, @RequestBody BinaryPersonDTO binaryPersonDTO) {
        RegistrationPerson person = registrationPersonRepository.findById(childId).orElseThrow();
        binaryRegistrationService.registerNewBinaryPerson(person, binaryPersonDTO.getBinaryPersonToPutItOnId(), binaryPersonDTO.isPreferredDirection());
    }

    @GetMapping("/income/{id}")
    public Map<SubscriptionPlan, BigDecimal> monthlyIncome(@PathVariable Long id) {
        return facadeService.monthlyIncome(id);
    }

    @PostMapping("/user/deposit")
    public void deposit(@RequestBody DepositDTO depositDTO) {
        facadeService.deposit(depositDTO.getId(), depositDTO.getMoney());
    }

    @PostMapping("/user/withdraw")
    public void withdraw(@RequestBody DepositDTO depositDTO) {
        facadeService.withdraw(depositDTO.getId(), depositDTO.getMoney());
    }


    @GetMapping("user/wallet/balance")
    public List<BigDecimal> balance(@RequestParam Long registrationPersonId) {
        return facadeService.wallet(registrationPersonId);
    }

    @PostMapping("/profile/edit")
    public void edit(@RequestBody EditPersonDTO editPersonDTO) {
        facadeService.editProfile(editPersonDTO);
    }

    private static RegistrationPerson customModelMapper(RegisterPersonDTO registerPersonDTO, RegistrationPerson parent) {
        RegistrationPerson person = new RegistrationPerson();
        person.setParent(parent);
        person.setName(registerPersonDTO.getName());
        person.setEmail(registerPersonDTO.getEmail());
        person.setPassword(registerPersonDTO.getPassword());
        return person;
    }
}
