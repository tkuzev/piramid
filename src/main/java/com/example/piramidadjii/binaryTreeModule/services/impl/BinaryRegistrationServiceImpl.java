package com.example.piramidadjii.binaryTreeModule.services.impl;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    @Override
    public void changePreferredDirection(BinaryPerson binaryPerson, boolean direction) {
        binaryPerson.setPreferredDirection(direction);
        binaryPersonRepository.save(binaryPerson);
    }
}
