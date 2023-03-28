package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.DistributeMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DistributeMoneyServiceImpl implements DistributeMoneyService {
    @Autowired
    BinaryPersonRepository binaryPersonRepository;
    @Override
    public void distributeMoney(BinaryPerson person, BigDecimal money) {
        if (person.getParent().getId() == 1L) {
            fillingContainer(person, money);
        } else {
            fillingContainer(person, money);
            distributeMoney(person.getParent(), money);
        }
    }

    //helper methods
    private void fillingContainer(BinaryPerson person, BigDecimal money) {
        if (person.getParent().getRightChild() == person) {
            person.getParent().setRightContainer(person.getParent().getRightContainer().add(money));
            binaryPersonRepository.save(person.getParent());
        } else if (person.getParent().getLeftChild() == person) {
            person.getParent().setLeftContainer(person.getParent().getLeftContainer().add(money));
            binaryPersonRepository.save(person.getParent());
        }
    }
}
