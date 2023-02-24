package com.example.piramidadjii;

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

@SpringBootApplication
@EnableScheduling
public class PiramidadjiiApplication {

    @Autowired
    private RegistrationTreeRepository registrationTreeRepository;

    @Autowired
    private BinaryTreeRepository binaryTreeRepository;

    @Autowired
    private BinaryTransactionRepository binaryTransactionRepository;

    public static void main(String[] args) {
        SpringApplication.run(PiramidadjiiApplication.class, args);
    }

    @Scheduled(cron = "00 00 00 1 * *", zone = "Europe/Sofia")
    public void binaryTree(){
        List<BinaryTree> binaryTreeList=binaryTreeRepository.findAll();
        binaryTreeList.forEach(this::updateMoney);
    }


    @Scheduled(cron = "00 00 00 * * *", zone = "Europe/Sofia")
    public void getTax() {
        List<RegistrationTree> allBySubscriptionExpirationDateFalse =
                registrationTreeRepository.getAllBySubscriptionExpirationDate(LocalDate.now());

        allBySubscriptionExpirationDateFalse.forEach(this::getTax);
    }

    private void getTax(RegistrationTree registrationTree) {

        BigDecimal newBalance = registrationTree.getBalance()
                .subtract(registrationTree.getSubscriptionPlan().getRegistrationFee());

        if (registrationTree.getIsSubscriptionEnabled() && newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            registrationTree.setBalance(newBalance);
            registrationTree.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
            registrationTreeRepository.save(registrationTree);
        } else {
            registrationTree.setIsSubscriptionEnabled(false);
            registrationTreeRepository.save(registrationTree);
        }

    }

    private void updateMoney(BinaryTree binaryTree) {

        if (binaryTree.getId()==1){
            return;
        }

        BigDecimal oldBalance=binaryTree.getBalance();
        BinaryTransaction binaryTransaction=new BinaryTransaction();

        if (binaryTree.getLeftContainer().compareTo(binaryTree.getRightContainer())<0){
            BigDecimal newBalance=oldBalance.add(binaryTree.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryTree.setBalance(newBalance);
            binaryTransaction.setPrice(binaryTree.getLeftContainer().multiply(BigDecimal.valueOf(0.05)));
        }else {
            BigDecimal newBalance=oldBalance.add(binaryTree.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
            binaryTree.setBalance(newBalance);
            binaryTransaction.setPrice(binaryTree.getRightContainer().multiply(BigDecimal.valueOf(0.05)));
        }

        binaryTransaction.setBinaryTree(binaryTree);
        binaryTransaction.setOperationType(OperationType.MONTHLY_BINARY_PERCENTAGE);
        binaryTransactionRepository.save(binaryTransaction);
        binaryTreeRepository.save(binaryTree);
    }

}
