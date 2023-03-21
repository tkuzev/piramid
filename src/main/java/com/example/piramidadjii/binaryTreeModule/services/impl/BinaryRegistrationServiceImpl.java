package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.baseModule.MailSenderService;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public void registerNewBinaryPerson(RegistrationPerson person, Long personToPutItOn, boolean preferredDirection) {
        addBinaryPerson(binParent(findSuitableParent(person)), person.getId(), personToPutItOn, preferredDirection);
    }

    @Override
    public void sendBinaryRegistrationEmail(RegistrationPerson registrationPerson, Long parentId) {
        try {
            mailSenderService.sendEmailWithoutAttachment(registrationPerson.getEmail(), "Chigani s mechove", "Mechove s chigani na: " + parentId);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public RegistrationPerson findSuitableParent(RegistrationPerson node) {
        Objects.requireNonNull(node, "nema node");
        RegistrationPerson parent = node.getParent();
        return (Objects.isNull(/*root*/parent.getParent())) ? parent : findSuitableParent(parent);
    }

    //todo i toq shte hodi da sviri
    private BinaryPerson createBinaryPerson(RegistrationPerson person) {
        BinaryPerson binPerson = new BinaryPerson(person.getId(), BigDecimal.ZERO, BigDecimal.ZERO);
        binPerson.setName(person.getName());
        binPerson.setBankAccount(person.getBankAccount());
        binPerson.setEmail(person.getEmail());
        binaryPersonRepository.save(binPerson);
        return binPerson;
    }

    //todo ???????????????
    private BinaryPerson binParent(RegistrationPerson parent) {
        return binaryPersonRepository.findById(parent.getId()).orElseThrow();
    }

    private void addBinaryPerson(BinaryPerson binParent, Long childId, Long toqDetoMuGoTikatId, boolean preferredSide) {

        RegistrationPerson child = registrationPersonRepository.findById(childId).orElseThrow();

        BinaryPerson binChild = createBinaryPerson(child);

        if (Objects.isNull(binParent)) return;

        if (Objects.equals(binParent.getId(), toqDetoMuGoTikatId)) {

            if (preferredSide) {
                binParent.setRightChild(binChild);
            } else {
                binParent.setLeftChild(binChild);
            }

            binChild.setParent(binParent);
            binaryPersonRepository.save(binParent);
            binaryPersonRepository.save(binChild);
            return;
        }

        addBinaryPerson(binParent.getRightChild(), childId, toqDetoMuGoTikatId, preferredSide);
        addBinaryPerson(binParent.getLeftChild(), childId, toqDetoMuGoTikatId, preferredSide);
    }
}
