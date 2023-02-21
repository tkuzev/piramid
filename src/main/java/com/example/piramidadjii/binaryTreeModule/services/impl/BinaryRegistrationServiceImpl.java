package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import org.springframework.stereotype.Service;


public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Override
    public void registerNewPerson(RegistrationTree person){
        //TODO proverqvamae rekutsivno dali person ima nqkude parent ot reg durvoto det e zapisan v binarnoto durvo,
        // sled tova namirame v binarnotot durvo tozi parent po email,
        // sled tova tursim na parenta v binarnotot durvo kude da zalepim persona pak rekursivno!
    }
}
