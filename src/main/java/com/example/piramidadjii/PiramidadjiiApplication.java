package com.example.piramidadjii;

import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryTransaction;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTransactionRepository;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.enums.OperationType;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class PiramidadjiiApplication {

    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    @Autowired
    private BinaryTransactionRepository binaryTransactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BigDecimal moneyToGive=BigDecimal.ZERO;

    public static void main(String[] args) {
        SpringApplication.run(PiramidadjiiApplication.class, args);
    }

    @Scheduled(cron = "00 00 00 1 * *", zone = "Europe/Sofia")
    public void binaryTree() {

        List<BinaryPerson> binaryPersonList = binaryPersonRepository.findAll();
        binaryPersonList.forEach(this::updateMoney);
        updateBossMoney(binaryPersonRepository.findById(1L).orElseThrow());
        moneyToGive=BigDecimal.ZERO;
    }

    private void updateBossMoney(BinaryPerson binaryPerson){

        binaryPerson.getBankAccount().setBalance(binaryPerson.getBankAccount().getBalance().add(binaryPerson.getRightContainer().add(binaryPerson.getLeftContainer())));
        BinaryTransaction winning=new BinaryTransaction();
        winning.setBinaryPerson(binaryPerson);
        winning.setPrice(binaryPerson.getRightContainer().add(binaryPerson.getLeftContainer()));
        winning.setOperationType(OperationType.MONTHLY_BINARY_TRANSACTION);
        BinaryTransaction lose=new BinaryTransaction();
        lose.setBinaryPerson(binaryPerson);
        lose.setPrice(moneyToGive.negate());
        lose.setOperationType(OperationType.MONTHLY_BINARY_TRANSACTION);
        binaryPerson.getBankAccount().setBalance(binaryPerson.getBankAccount().getBalance().subtract(moneyToGive));
        binaryPerson.setLeftContainer(BigDecimal.ZERO);
        binaryPerson.setRightContainer(BigDecimal.ZERO);
        binaryPersonRepository.save(binaryPerson);
        bankAccountRepository.save(binaryPerson.getBankAccount());
        binaryTransactionRepository.save(winning);
        binaryTransactionRepository.save(lose);
    }

    private void updateMoney(BinaryPerson binaryPerson) {
        if (binaryPerson.getId()==1){
            return;
        }

        BigDecimal oldBalance = binaryPerson.getBankAccount().getBalance();
        BinaryTransaction binaryTransaction = new BinaryTransaction();

        if (binaryPerson.getLeftContainer().compareTo(binaryPerson.getRightContainer()) < 0) {
            BigDecimal newBalance = oldBalance.add(binaryPerson.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryPerson.getBankAccount().setBalance(newBalance);
            moneyToGive = moneyToGive.add(binaryPerson.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryTransaction.setPrice(binaryPerson.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
        } else {
            BigDecimal newBalance = oldBalance.add(binaryPerson.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryPerson.getBankAccount().setBalance(newBalance);
            binaryTransaction.setPrice(binaryPerson.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            moneyToGive=moneyToGive.add(binaryPerson.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
        }

        binaryPerson.setRightContainer(BigDecimal.ZERO);
        binaryPerson.setLeftContainer(BigDecimal.ZERO);
        binaryPersonRepository.save(binaryPerson);

        binaryTransaction.setBinaryPerson(binaryPerson);
        binaryTransaction.setOperationType(OperationType.MONTHLY_BINARY_PERCENTAGE);
        binaryTransactionRepository.save(binaryTransaction);
        bankAccountRepository.save(binaryPerson.getBankAccount());
    }




    @Scheduled(cron = "00 00 00 * * *", zone = "Europe/Sofia")
    public void getTax() {
        List<RegistrationPerson> allBySubscriptionExpirationDateFalse =
                registrationPersonRepository.getAllBySubscriptionExpirationDate(LocalDate.now());

        allBySubscriptionExpirationDateFalse.forEach(this::getTax);

    }

    private void getTax(RegistrationPerson registrationPerson) {

        BigDecimal newBalance = registrationPerson.getBankAccount().getBalance()
                .subtract(registrationPerson.getSubscriptionPlan().getRegistrationFee());

        if (registrationPerson.getIsSubscriptionEnabled() && newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            registrationPerson.getBankAccount().setBalance(newBalance);
            registrationPerson.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
            registrationPersonRepository.save(registrationPerson);
        } else {
            registrationPerson.setIsSubscriptionEnabled(false);
            registrationPersonRepository.save(registrationPerson);
        }

    }

}
