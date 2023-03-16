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

    @Override
    public void registerNewBinaryPerson(RegistrationPerson person, Long personToPutItOn, boolean preferredDirection) {
         addBinaryPerson(binParent(findSuitableParent(person)), createBinaryPerson(person), personToPutItOn,preferredDirection);
    }

    public RegistrationPerson findSuitableParent(RegistrationPerson node) {
        Objects.requireNonNull(node,"nema node");
        RegistrationPerson parent = node.getParent();

        return (Objects.isNull(/*root*/parent.getParent())) ? parent: findSuitableParent(parent);
    }
    private BinaryPerson createBinaryPerson(RegistrationPerson person) {
        BinaryPerson binPerson = new BinaryPerson(person.getId(), BigDecimal.ZERO, BigDecimal.ZERO);
        binPerson.setName(person.getName());
        binPerson.setBankAccount(person.getBankAccount());
        binPerson.setEmail(person.getEmail());
        binaryPersonRepository.save(binPerson);
        return binPerson;
    }
    private BinaryPerson binParent(RegistrationPerson parent) {
        return binaryPersonRepository.findById(parent.getId()).orElseThrow();
    }

    //todo da ima i oshthe edin chovek koito e choveka pod koito shte slojim noviq chovek, validachiq dali e svobodna pozichiqta
    private void addBinaryPerson(BinaryPerson binParent, BinaryPerson binChild, Long toqDetoMuGoTikatId, boolean preferredSide ) {

        if(Objects.isNull(binParent)) return;

        if(Objects.equals(binParent.getId(), toqDetoMuGoTikatId)){

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

        addBinaryPerson(binParent.getRightChild(), binChild, toqDetoMuGoTikatId, preferredSide);
        addBinaryPerson(binParent.getLeftChild(), binChild, toqDetoMuGoTikatId, preferredSide);
    }
}
