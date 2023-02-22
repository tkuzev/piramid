package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTreeRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;


public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryTreeRepository binaryTreeRepository;

    @Override
    public void registerNewPerson(RegistrationTree person) {
        addBinaryPerson(binParent(findParent(person)), createBinaryPerson(person), true);
    }

    public RegistrationTree findParent(RegistrationTree node) {
        if (Objects.isNull(node)) {
            throw new RuntimeException("nema node");
        }
        if (node.getParent().getSubscriptionPlan().isEligibleForBinary() || Objects.isNull(/*root*/node.getParent().getParent())) {
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

    private BinaryTree binParent(RegistrationTree parent){
        return binaryTreeRepository.findByEmail(parent.getEmail()).orElseThrow();

    }

    private void addBinaryPerson(BinaryTree binParent, BinaryTree binChild, boolean direction) {
        if (direction/*right*/) {
            if (!Objects.isNull(binParent.getRightChild())) {
                addBinaryPerson(binParent.getRightChild(), binChild, Direction()/*nqkoga nema da e random moje bi*/);
            }
            binParent.setRightChild(binChild);
        }
        /*left*/
        if (!Objects.isNull(binParent.getLeftChild())) {
            addBinaryPerson(binParent.getLeftChild(), binChild, Direction()/*nqkoga nema da e random moje bi*/);
        }
        binParent.setLeftChild(binChild);

    }
    private boolean Direction() {
        return new Random().nextBoolean();
    }
}

//TODO proverqvamae rekutsivno dali person ima nqkude parent ot reg durvoto det e zapisan v binarnoto durvo,
// sled tova namirame v binarnotot durvo tozi parent po email,
// sled tova tursim na parenta v binarnotot durvo kude da zalepim persona pak rekursivno!