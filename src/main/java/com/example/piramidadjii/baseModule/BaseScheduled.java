package com.example.piramidadjii.baseModule;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableScheduling
public class BaseScheduled {

    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BigDecimal moneyToGive = BigDecimal.ZERO;

    private static final long HELPER_BANK_ID = -1;



    @Scheduled(cron = "00 00 00 1 * *", zone = "Europe/Sofia")
    public void binaryTree() {

        List<BinaryPerson> binaryPersonList = binaryPersonRepository.findAll();
        binaryPersonList.forEach(this::updateMoney);
        updateBossMoney();
        moneyToGive = BigDecimal.ZERO;
    }

    private void updateBossMoney() {

        BankAccount helperBankAccount=bankAccountRepository.findById(HELPER_BANK_ID).orElseThrow();
        BinaryPerson boss = binaryPersonRepository.findById(1L).orElseThrow();
        boss.getBankAccount().setBalance(boss.getBankAccount().getBalance().add(boss.getRightContainer().add(boss.getLeftContainer())));
        Bank debitTransaction = new Bank();
        debitTransaction.setDstAccId(boss.getBankAccount());
        //todo v sqkla da se dobavi pomoshten bankov akaunt s id -1
        debitTransaction.setAmount(boss.getRightContainer().add(boss.getLeftContainer()));
        debitTransaction.setOperationType(OperationType.DT);
        debitTransaction.setDescription(Description.MONTHLY_BINARY_TRANSACTION);
        Bank creditTransaction = new Bank();
        creditTransaction.setSrcAccId(helperBankAccount);
        creditTransaction.setAmount(moneyToGive);
        creditTransaction.setOperationType(OperationType.CT);
        creditTransaction.setDescription(Description.MONTHLY_BINARY_TRANSACTION);
        boss.getBankAccount().setBalance(boss.getBankAccount().getBalance().subtract(moneyToGive));
        boss.setLeftContainer(BigDecimal.ZERO);
        boss.setRightContainer(BigDecimal.ZERO);
        binaryPersonRepository.save(boss);
        bankAccountRepository.save(boss.getBankAccount());
        bankRepository.save(debitTransaction);
        bankRepository.save(creditTransaction);
    }

    private void updateMoney(BinaryPerson binaryPerson) {
        if (binaryPerson.getId() == 1) {
            return;
        }

        BigDecimal oldBalance = binaryPerson.getBankAccount().getBalance();

        Bank debitTransaction = new Bank();
        Bank creditTransaction = new Bank();
        BankAccount helperBankAccount = bankAccountRepository.findById(HELPER_BANK_ID).orElseThrow();
        BigDecimal oldHelperBalance = helperBankAccount.getBalance();

        if (binaryPerson.getLeftContainer().compareTo(binaryPerson.getRightContainer()) < 0) {
            BigDecimal newBalance = oldBalance.add(binaryPerson.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            BigDecimal newHelperBalance = oldHelperBalance.subtract(binaryPerson.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryPerson.getBankAccount().setBalance(newBalance);
            helperBankAccount.setBalance(newHelperBalance);
            moneyToGive = moneyToGive.add(binaryPerson.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            creditTransaction.setAmount(binaryPerson.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            debitTransaction.setAmount(binaryPerson.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));

        } else {
            BigDecimal newBalance = oldBalance.add(binaryPerson.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            BigDecimal newHelperBalance = oldHelperBalance.subtract(binaryPerson.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryPerson.getBankAccount().setBalance(newBalance);
            helperBankAccount.setBalance(newHelperBalance);
            moneyToGive = moneyToGive.add(binaryPerson.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            creditTransaction.setAmount(binaryPerson.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            debitTransaction.setAmount(binaryPerson.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
        }

        binaryPerson.setRightContainer(BigDecimal.ZERO);
        binaryPerson.setLeftContainer(BigDecimal.ZERO);
        binaryPersonRepository.save(binaryPerson);

        creditTransaction.setSrcAccId(helperBankAccount);
        creditTransaction.setOperationType(OperationType.CT);
        creditTransaction.setDescription(Description.MONTHLY_BINARY_PERCENTAGE);
        bankRepository.save(creditTransaction);
        bankAccountRepository.save(helperBankAccount);

        debitTransaction.setDstAccId(binaryPerson.getBankAccount());
        debitTransaction.setOperationType(OperationType.DT);
        debitTransaction.setDescription(Description.MONTHLY_BINARY_PERCENTAGE);
        bankRepository.save(debitTransaction);
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
