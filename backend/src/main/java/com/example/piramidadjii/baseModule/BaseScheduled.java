package com.example.piramidadjii.baseModule;

import com.example.piramidadjii.bankAccountModule.entities.Bank;
import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import com.example.piramidadjii.bankAccountModule.repositories.BankAccountRepository;
import com.example.piramidadjii.bankAccountModule.repositories.BankRepository;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


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

    @Autowired
    ConfigurationService configurationService;

    private BigDecimal moneyToGive = BigDecimal.ZERO;

    private static final long HELPER_BANK_ID = -1;


    @Autowired
    private MailSenderService mailSenderService;
    private final DateTimeFormatter formatter;
    private final LocalDate date;

    public BaseScheduled() {
        this.date = LocalDate.now();
        this.formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("bg"));
    }

    @Scheduled(cron = "00 00 00 1 * *", zone = "Europe/Sofia")
    public void binaryTree() {
        List<BinaryPerson> binaryPersonList = binaryPersonRepository.findAll();
        binaryPersonList.forEach(this::updateMoney);
        updateBossMoney();
        moneyToGive = BigDecimal.ZERO;
    }

    private void updateBossMoney() {

        BankAccount helperBankAccount = bankAccountRepository.findById(HELPER_BANK_ID).orElseThrow();
        BinaryPerson boss = binaryPersonRepository.findById(1L).orElseThrow();
        boss.getBankAccount().setBalance(boss.getBankAccount().getBalance().add(boss.getRightContainer().add(boss.getLeftContainer())));
        Bank debitTransaction = new Bank();

        debitTransaction.setDstAccId(boss.getBankAccount());
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

    @Transactional
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

    @SneakyThrows
    private void getTax(RegistrationPerson registrationPerson) {
        BigDecimal newBalance = registrationPerson.getBankAccount().getBalance()
                .subtract(registrationPerson.getSubscriptionPlan().getRegistrationFee());

        if (registrationPerson.getIsSubscriptionEnabled() && newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            registrationPerson.getBankAccount().setBalance(newBalance);
            registrationPerson.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
            registrationPersonRepository.save(registrationPerson);
            generatePDF(registrationPerson);
            String message = "Успешно бе платена таксата за месец " + date.format(formatter) + ".";
            mailSenderService.sendEmail(registrationPerson.getEmail(), "Платена такса", message, "reciept.pdf");
            File file = new File("reciept.pdf");
            file.delete();
        } else {
            mailSenderService.sendEmailWithoutAttachment(registrationPerson.getEmail(), "Неуспешно плащане на месечен абонамент", "Недостатъчен баланс за заплащане на месечната такса.");
            registrationPerson.setIsSubscriptionEnabled(false);
            registrationPersonRepository.save(registrationPerson);
        }

    }

    private void generatePDF(RegistrationPerson registrationPerson) throws IOException {

        BankAccount bankAccount = bankAccountRepository.findById(-1L).orElseThrow();
        Bank transaction = configurationService.transactionBoiler(bankAccount, registrationPerson,
                registrationPerson.getSubscriptionPlan(),
                Description.REGISTRATION_FEE,
                registrationPerson.getSubscriptionPlan().getRegistrationFee());

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder.append("Cenata na transakciqta e: ").append(transaction.getAmount()).append(" izvurshena na: ").append(transaction.getTransactionDate());
        stringBuilder1.append("Ostanalite pari v smetkata: ").append(bankAccount.getBalance());

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.newLineAtOffset(140, 750);
        contentStream.showText(stringBuilder.toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.newLineAtOffset(140, 700);
        contentStream.showText(stringBuilder1.toString());
        contentStream.endText();
        contentStream.close();

        document.save("reciept.pdf");
        document.close();
    }
}
