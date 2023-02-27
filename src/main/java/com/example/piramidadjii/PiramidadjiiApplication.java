package com.example.piramidadjii;

import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.baseEntities.Transaction;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryTransaction;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTransactionRepository;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTreeRepository;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.enums.OperationType;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import com.example.piramidadjii.registrationTreeModule.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableScheduling
public class PiramidadjiiApplication {

    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;

    @Autowired
    private BinaryTreeRepository binaryTreeRepository;

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

        List<BinaryTree> binaryTreeList = binaryTreeRepository.findAll();
        binaryTreeList.forEach(this::updateMoney);
        updateBossMoney(binaryTreeRepository.findById(1L).orElseThrow());
        moneyToGive=BigDecimal.ZERO;
    }

    private void updateBossMoney(BinaryTree binaryTree){

        binaryTree.getBankAccount().setBalance(binaryTree.getBankAccount().getBalance().add(binaryTree.getRightContainer().add(binaryTree.getLeftContainer())));
        BinaryTransaction winning=new BinaryTransaction();
        winning.setBinaryTree(binaryTree);
        winning.setPrice(binaryTree.getRightContainer().add(binaryTree.getLeftContainer()));
        winning.setOperationType(OperationType.MONTHLY_BINARY_TRANSACTION);
        BinaryTransaction lose=new BinaryTransaction();
        lose.setBinaryTree(binaryTree);
        lose.setPrice(moneyToGive.negate());
        lose.setOperationType(OperationType.MONTHLY_BINARY_TRANSACTION);
        binaryTree.getBankAccount().setBalance(binaryTree.getBankAccount().getBalance().subtract(moneyToGive));
        binaryTree.setLeftContainer(BigDecimal.ZERO);
        binaryTree.setRightContainer(BigDecimal.ZERO);
        binaryTreeRepository.save(binaryTree);
        bankAccountRepository.save(binaryTree.getBankAccount());
        binaryTransactionRepository.save(winning);
        binaryTransactionRepository.save(lose);
    }

    private void updateMoney(BinaryTree binaryTree) {
        if (binaryTree.getId()==1){
            return;
        }

        BigDecimal oldBalance = binaryTree.getBankAccount().getBalance();
        BinaryTransaction binaryTransaction = new BinaryTransaction();

        if (binaryTree.getLeftContainer().compareTo(binaryTree.getRightContainer()) < 0) {
            BigDecimal newBalance = oldBalance.add(binaryTree.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryTree.getBankAccount().setBalance(newBalance);
            moneyToGive = moneyToGive.add(binaryTree.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryTransaction.setPrice(binaryTree.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
        } else {
            BigDecimal newBalance = oldBalance.add(binaryTree.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryTree.getBankAccount().setBalance(newBalance);
            binaryTransaction.setPrice(binaryTree.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            moneyToGive=moneyToGive.add(binaryTree.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
        }

        binaryTree.setRightContainer(BigDecimal.ZERO);
        binaryTree.setLeftContainer(BigDecimal.ZERO);
        binaryTreeRepository.save(binaryTree);

        binaryTransaction.setBinaryTree(binaryTree);
        binaryTransaction.setOperationType(OperationType.MONTHLY_BINARY_PERCENTAGE);
        binaryTransactionRepository.save(binaryTransaction);
        bankAccountRepository.save(binaryTree.getBankAccount());
    }




    @Scheduled(cron = "00 00 00 * * *", zone = "Europe/Sofia")
    public void getTax() {
        List<RegistrationTree> allBySubscriptionExpirationDateFalse =
                registrationTreeRepository.getAllBySubscriptionExpirationDate(LocalDate.now());

        allBySubscriptionExpirationDateFalse.forEach(this::getTax);

    }

    private void getTax(RegistrationTree registrationTree) {

        BigDecimal newBalance = registrationTree.getBankAccount().getBalance()
                .subtract(registrationTree.getSubscriptionPlan().getRegistrationFee());

        if (registrationTree.getIsSubscriptionEnabled() && newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            registrationTree.getBankAccount().setBalance(newBalance);
            registrationTree.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
            registrationTreeRepository.save(registrationTree);
        } else {
            registrationTree.setIsSubscriptionEnabled(false);
            registrationTreeRepository.save(registrationTree);
        }

    }

}
