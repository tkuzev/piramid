package com.example.piramidadjii.facade.controller;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.facade.FacadeService;
import com.example.piramidadjii.facade.dto.*;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
public class FacadeController extends BaseController {
    @Autowired
    private FacadeService facadeService;
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/user/sell")
    public void makeSell(@RequestBody SellDTO sellDTO) {
        facadeService.createTransaction(sellDTO.getId(), sellDTO.getPrice());
    }


    @PostMapping("/user/register/registrationTree")
    public void registerPerson(@RequestBody RegisterPersonDTO registerPersonDTO) {
        BigDecimal money = registerPersonDTO.getMoney();
        facadeService.registerPerson(
                registerPersonDTO.getParentId(),
                registerPersonDTO.getName(),
                registerPersonDTO.getEmail(),
                registerPersonDTO.getPassword(),
                money
        );
    }

    @PostMapping("/user/subscriptionPlan/upgrade")
    public void upgradeSubscriptionPlan(UpgradeSubscriptionPlanDTO upgradeSubscriptionPlanDTO){
        facadeService.upgradeSubscriptionPlan(upgradeSubscriptionPlanDTO.getRegistrationPersonId(),upgradeSubscriptionPlanDTO.getSubscriptionPlan());
    }

    @PostMapping("/user/register/binary/{childId}")
    public void registerBinaryPerson(@PathVariable Long childId, @RequestBody BinaryPersonDTO binaryPersonDTO) {
        facadeService.registerNewBinaryPerson(childId, binaryPersonDTO.getBinaryPersonToPutItOnId(), binaryPersonDTO.isPreferredDirection());
    }

    @GetMapping("/user/income")
    public Map<String, BigDecimal> monthlyIncome(@RequestParam("id") Long id) {
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

    @PutMapping("/user/edit")
    public void edit(@RequestBody EditPersonDTO editPersonDTO) {
        facadeService.editProfile(customModelMapperShowUserData(editPersonDTO));
    }

    @GetMapping("user/email")
    public String username(@RequestBody tokenDTO tokenDTO) {
        return facadeService.getEmailFromJWT(tokenDTO.getToken());
    }

    @GetMapping("/getTree")
    public ResponseEntity<Set<BinaryDTO>> getTree(@RequestParam Long id) {
        Map<BinaryPerson, Boolean> tree = facadeService.getTree(id);

        Set<BinaryDTO> dtoTree = new HashSet<>();
        tree.forEach((k, v) -> {
            BinaryDTO binaryDTO = modelMapper.map(k, BinaryDTO.class);
            binaryDTO.setDirection(v);
            dtoTree.add(binaryDTO);
        });

        return new ResponseEntity<>(dtoTree, HttpStatus.OK);
    }

    @GetMapping("/binary/getById")
    public BinaryDTO getBinaryById(@RequestParam Long id) {
        BinaryPerson binaryPersonById = facadeService.getBinaryPersonById(id);
        return modelMapper.map(binaryPersonById, BinaryDTO.class);
    }

    @GetMapping("/registration/getById")
    public ResponseEntity<RegisterPersonDTO> getRegistrationPersonById(@RequestParam Long id) {
        RegistrationPerson registrationPersonById = facadeService.getRegistrationPersonById(id);
        RegisterPersonDTO dto = modelMapper.map(registrationPersonById, RegisterPersonDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/user/getPersonId")
    public Long getPersonId(@RequestParam("email") String email) {
        return facadeService.getRegistrationPersonByEmail(email).getId();
    }

    @GetMapping("/user/getPersonDetails")
    public RegisterPersonDTO getPersonDetails(@RequestParam("email") String email) {
        RegistrationPerson person = facadeService.displayPersonDetails(email);
        return modelMapper.map(person, RegisterPersonDTO.class);
    }

    @GetMapping("/user/getAllSubscriptionPlans")
    public List<SubscriptionPlanDTO> getAllRegistrationPlans() {
        return facadeService.getAllSubscriptionPlans().stream().map(plan -> modelMapper.map(plan, SubscriptionPlanDTO.class)).toList();
    }

    private RegistrationPerson customModelMapperShowUserData(EditPersonDTO editPersonDTO) {
        RegistrationPerson person = new RegistrationPerson();
        person.setId(editPersonDTO.getId());
        person.setName(editPersonDTO.getName());
        person.setEmail(editPersonDTO.getEmail());
        person.setSubscriptionEnabled(editPersonDTO.isSubscriptionEnabled());
        return person;
    }


}
