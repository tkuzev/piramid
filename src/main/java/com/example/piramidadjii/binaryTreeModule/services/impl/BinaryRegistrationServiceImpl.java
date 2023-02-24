package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTreeRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryTreeRepository binaryTreeRepository;

    //main methods
    @Override
    public BinaryTree registerNewPerson(RegistrationTree person, boolean preferredDirection) {
        return addBinaryPerson(binParent(findParent(person)), createBinaryPerson(person, preferredDirection));
    }

    @Override
    public void changePreferredDirection(BinaryTree binaryTree, boolean direction) {
        binaryTree.setPreferredDirection(direction);
        binaryTreeRepository.save(binaryTree);
    }


    public RegistrationTree findParent(RegistrationTree node) {
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
    private BinaryTree createBinaryPerson(RegistrationTree person, boolean preferredDirection) {
        BinaryTree binPerson = new BinaryTree();
        binPerson.setName(person.getName());
        binPerson.setEmail(person.getEmail());
        binPerson.setRightContainer(BigDecimal.ZERO);
        binPerson.setLeftContainer(BigDecimal.ZERO);
        binPerson.setPreferredDirection(preferredDirection);
        binaryTreeRepository.save(binPerson);
        return binPerson;
    }

    private BinaryTree binParent(RegistrationTree parent) {
        return binaryTreeRepository.findByEmail(parent.getEmail()).orElseThrow();
    }

    private BinaryTree addBinaryPerson(BinaryTree binParent, BinaryTree binChild) {
        if (binParent.isPreferredDirection()/*right*/) {
            if (Objects.isNull(binParent.getRightChild())) {
                binParent.setRightChild(binChild);
                binChild.setParent(binParent);
                binaryTreeRepository.save(binParent);
            } else {
                addBinaryPerson(binParent.getRightChild(), binChild);
            }
        } else {
            if (Objects.isNull(binParent.getLeftChild())) {
                binParent.setLeftChild(binChild);
                binChild.setParent(binParent);
                binaryTreeRepository.save(binParent);
            } else {
                addBinaryPerson(binParent.getLeftChild(), binChild);
            }
        }

        binaryTreeRepository.save(binChild);
        return binChild;
    }

}
