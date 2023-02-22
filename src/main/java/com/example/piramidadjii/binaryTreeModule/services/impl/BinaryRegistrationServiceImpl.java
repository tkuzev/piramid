package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTreeRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryTreeRepository binaryTreeRepository;

    @Override
    public void registerNewPerson(RegistrationTree person) {
        addBinaryPerson(binParent(findParent(person)), createBinaryPerson(person));
    }

    public RegistrationTree findParent(RegistrationTree node) {
        if (Objects.isNull(node)) {
            throw new RuntimeException("nema node");
        }
        if (Objects.isNull(/*root*/node.getParent().getParent()) || node.getParent().getSubscriptionPlan().isEligibleForBinary()) {
            return node.getParent();
        }
        findParent(node.getParent());
        throw new RuntimeException("Na maika ti");
    }


    //helper methods
    private BinaryTree createBinaryPerson(RegistrationTree person) {
        BinaryTree binPerson = new BinaryTree();
        binPerson.setName(person.getName());
        binPerson.setEmail(person.getEmail());
        binPerson.setRightContainer(BigDecimal.ZERO);
        binPerson.setLeftContainer(BigDecimal.ZERO);
        binaryTreeRepository.save(binPerson);
        return binPerson;
    }

    private BinaryTree binParent(RegistrationTree parent) {
        return binaryTreeRepository.findByEmail(parent.getEmail()).orElseThrow();
    }

    private void addBinaryPerson(BinaryTree binParent, BinaryTree binChild) {
        if (binParent.isPreferredDirection()/*right*/) {
            if (Objects.isNull(binParent.getRightChild())) {
                binParent.setRightChild(binChild);
            } else {
                addBinaryPerson(binParent.getRightChild(), binChild);
            }
        } else { // AKO NQMA ELSE ADD BINARY PERSON SE VIKA AMA POSLE SE VRUSHTA DA DOVURSHVA I STAVA STACK OVERFLOW
            if (Objects.isNull(binParent.getLeftChild())) {
                binParent.setLeftChild(binChild);
            } else {
                addBinaryPerson(binParent.getLeftChild(), binChild);
            }
        }
        binaryTreeRepository.save(binParent);
    }
}
