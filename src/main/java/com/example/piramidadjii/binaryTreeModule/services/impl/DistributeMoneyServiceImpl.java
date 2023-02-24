package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.entities.BinaryTree;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryTreeRepository;
import com.example.piramidadjii.binaryTreeModule.services.DistributeMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class DistributeMoneyServiceImpl implements DistributeMoneyService {
    @Autowired
    BinaryTreeRepository binaryTreeRepository;

    @Override
    public void distributeMoney(BinaryTree person, BigDecimal money){
        if(person.getParent().getId() == 1L){
            fillingContainer(person, money);
        }
        else{
            fillingContainer(person, money);
            distributeMoney(person.getParent(), money);
        }
    };

    //helper methods
    private void fillingContainer(BinaryTree person, BigDecimal money) {
        if(person.getParent().getRightChild() == person){
            person.getParent().setRightContainer(person.getParent().getRightContainer().add(money));
            binaryTreeRepository.save(person);
        }

        else if(person.getParent().getLeftChild() == person){
            person.getParent().setLeftContainer(person.getParent().getLeftContainer().add(money));
            binaryTreeRepository.save(person);
        }
    }
}
