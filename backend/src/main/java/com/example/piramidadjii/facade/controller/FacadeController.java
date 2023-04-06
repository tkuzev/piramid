package com.example.piramidadjii.facade.controller;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.facade.dto.*;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.SubscriptionPlan;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.SubscriptionPlanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


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
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;


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
    public List<BigDecimal> balance(@RequestParam("id") Long registrationPersonId) {
        return facadeService.wallet(registrationPersonId);
    }

    @PostMapping("/profile/edit")
    public void edit(@RequestBody EditPersonDTO editPersonDTO) {
        SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findById(editPersonDTO.getSubscriptionPlanId()).orElseThrow();
        System.out.println(subscriptionPlan.getName());
        facadeService.editProfile(customModelMapperShowUserData(editPersonDTO), subscriptionPlan);
    }

    @GetMapping("user/email")
    public String username(@RequestBody tokenDTO tokenDTO) {
        return facadeService.getEmailFromJWT(tokenDTO.getToken());
    }

    @GetMapping("/getTree")
    public ResponseEntity<Set<BinaryDTO>> getTree(@RequestParam Long id) {
        BinaryPerson binaryPerson = binaryPersonRepository.findById(id).orElseThrow();
        Map<BinaryPerson, Boolean> tree = facadeService.getTree(binaryPerson);

        Set<BinaryDTO> dtoTree=new HashSet<>();
        tree.forEach((k,v) -> {
            BinaryDTO binaryDTO = modelMapper.map(k, BinaryDTO.class);
            binaryDTO.setDirection(v);
            dtoTree.add(binaryDTO);
        });
        return new ResponseEntity<>(dtoTree, HttpStatus.OK);
    }

    @GetMapping("/binary/getById")
    public BinaryDTO getBinaryById(@RequestParam Long id) {
        BinaryPerson binaryPerson = binaryPersonRepository.findById(id).orElseThrow();
        return modelMapper.map(binaryPerson, BinaryDTO.class);
    }

    @GetMapping("/registration/getById")
    public ResponseEntity<RegisterPersonDTO> getRegistrationPersonById(@RequestParam Long id) {
        RegistrationPerson registrationPerson = registrationPersonRepository.findById(id).orElseThrow();
        RegisterPersonDTO dto = modelMapper.map(registrationPerson, RegisterPersonDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/user/getPersonId")
    public Long getPersonId(@RequestParam("email") String email) {
        RegistrationPerson registrationPerson = registrationPersonRepository.findByEmail(email).orElseThrow();
        return registrationPerson.getId();
    }

    @GetMapping("/user/getPersonDetails")
    public RegisterPersonDTO getPersonDetails(@RequestParam("email") String email){
        registrationPersonRepository.findByEmail(email);
        RegistrationPerson person = facadeService.displayPersonDetails(email);
        return modelMapper.map(person,RegisterPersonDTO.class);
    }

    private  RegistrationPerson customModelMapperShowUserData(EditPersonDTO editPersonDTO){
        RegistrationPerson person=new RegistrationPerson();
        person.setId(editPersonDTO.getId());
        person.setName(editPersonDTO.getName());
        person.setEmail(editPersonDTO.getEmail());
        person.setPassword(editPersonDTO.getPassword());
        person.setIsSubscriptionEnabled(editPersonDTO.isSubscriptionEnabled());
        return person;
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
