package com.example.piramidadjii.facade.controller;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.facade.dto.*;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class FacadeController {
    @Autowired
    private FacadeService facadeService;
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private BinaryRegistrationService binaryRegistrationService;
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    @PostMapping("/user/sell")
    public void makeSell(@RequestBody SellDTO sellDTO) {

        RegistrationPerson person = registrationPersonRepository.findById(sellDTO.getId()).orElseThrow();
        facadeService.createTransaction(person, sellDTO.getPrice());
    }

    @PostMapping("/user/register/registrationTree")
    public void registerPerson(@RequestBody RegisterPersonDTO registerPersonDTO) {
        RegistrationPerson parent = registrationPersonRepository.findById(registerPersonDTO.getParentId()).orElseThrow();
        BigDecimal money = registerPersonDTO.getMoney();
        RegistrationPerson person = customModelMapper(registerPersonDTO, parent);
        facadeService.registerPerson(person, money);
    }

    @PostMapping("/user/register/binary/{childId}")
    public void registerBinaryPerson(@PathVariable Long childId, @RequestBody BinaryPersonDTO binaryPersonDTO) {
        RegistrationPerson person = registrationPersonRepository.findById(childId).orElseThrow();
        binaryRegistrationService.registerNewBinaryPerson(person, binaryPersonDTO.getBinaryPersonToPutItOnId(), binaryPersonDTO.isPreferredDirection());
    }

    @GetMapping("/user/income/{id}")
    public Map<String, BigDecimal> monthlyIncome(@PathVariable Long id) {
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

    @GetMapping("user/email")
    public String username(@RequestBody tokenDTO tokenDTO) {
        return facadeService.getEmailFromJWT(tokenDTO.getToken());
    }

    @GetMapping("/getTree")
    public List<BinaryPerson> getTree(@RequestParam BinaryDTO binaryDTO){
        BinaryPerson binaryPersonRepositoryByEmail = binaryPersonRepository.findByEmail(binaryDTO.getEmail()).orElseThrow();
        return facadeService.getTree(binaryPersonRepositoryByEmail);
    }
    @GetMapping("binary/getById/")
    public BinaryPerson getBinaryById(@RequestParam Long id){
        BinaryPerson binaryPerson = binaryPersonRepository.findById(id).orElseThrow();
        return binaryPerson;
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
