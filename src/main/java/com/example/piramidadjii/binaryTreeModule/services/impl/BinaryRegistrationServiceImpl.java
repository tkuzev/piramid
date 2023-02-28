package com.example.piramidadjii.binaryTreeModule.services.impl;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    //main methods
    @Override
    public BinaryPerson registerNewPerson(RegistrationPerson person, boolean preferredDirection) {
        return addBinaryPerson(binParent(findParent(person)), createBinaryPerson(person, preferredDirection));
    }

    @Override
    public void changePreferredDirection(BinaryPerson binaryPerson, boolean direction) {
        binaryPerson.setPreferredDirection(direction);
        binaryPersonRepository.save(binaryPerson);
    }


    public RegistrationPerson findParent(RegistrationPerson node) {
        if (Objects.isNull(node)) {
            throw new RuntimeException("nema node");
        }
        if (Objects.isNull(/*root*/node.getParent().getParent()) || node.getParent().getSubscriptionPlan().isEligibleForBinary()) {
            return node.getParent();
        }
        else {
            return findParent(node.getParent());
        }
    }


    //helper methods
    private BinaryPerson createBinaryPerson(RegistrationPerson person, boolean preferredDirection) {
        BinaryPerson binPerson = new BinaryPerson();
        binPerson.setBankAccount(person.getBankAccount());
        binPerson.setName(person.getName());
        binPerson.setEmail(person.getEmail());
        binPerson.setRightContainer(BigDecimal.ZERO);
        binPerson.setLeftContainer(BigDecimal.ZERO);
        binPerson.setPreferredDirection(preferredDirection);
        binaryPersonRepository.save(binPerson);
        return binPerson;
    }

    private BinaryPerson binParent(RegistrationPerson parent) {
        return binaryPersonRepository.findByEmail(parent.getEmail()).orElseThrow();
    }

    private BinaryPerson addBinaryPerson(BinaryPerson binParent, BinaryPerson binChild) {
        if (binParent.isPreferredDirection()/*right*/) {
            if (Objects.isNull(binParent.getRightChild())) {
                binParent.setRightChild(binChild);
                binChild.setParent(binParent);
                binaryPersonRepository.save(binParent);
            } else {
                addBinaryPerson(binParent.getRightChild(), binChild);
            }
        } else {
            if (Objects.isNull(binParent.getLeftChild())) {
                binParent.setLeftChild(binChild);
                binChild.setParent(binParent);
                binaryPersonRepository.save(binParent);
            } else {
                addBinaryPerson(binParent.getLeftChild(), binChild);
            }
        }

        binaryPersonRepository.save(binChild);
        return binChild;
    }

}
