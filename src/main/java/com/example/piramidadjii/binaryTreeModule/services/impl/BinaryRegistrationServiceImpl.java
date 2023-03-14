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
    public BinaryPerson registerNewBinaryPerson(RegistrationPerson person, boolean preferredDirection) {
        return addBinaryPerson(binParent(findSuitableParent(person)), createBinaryPerson(person, preferredDirection));
    }

    public RegistrationPerson findSuitableParent(RegistrationPerson node) {
        Objects.requireNonNull(node,"nema node");
        RegistrationPerson parent = node.getParent();

        return (Objects.isNull(/*root*/parent.getParent())) ? parent: findSuitableParent(parent);
    }

    private BinaryPerson createBinaryPerson(RegistrationPerson person, boolean preferredDirection) {
        BinaryPerson binPerson = new BinaryPerson(person.getId(), BigDecimal.ZERO, BigDecimal.ZERO, preferredDirection);
        binPerson.setName(person.getName());
        binPerson.setEmail(person.getEmail());
        binPerson.setPreferredDirection(preferredDirection);
        binaryPersonRepository.save(binPerson);
        return binPerson;
    }

    private BinaryPerson binParent(RegistrationPerson parent) {
        return binaryPersonRepository.findById(parent.getId()).orElseThrow();
    }

    //todo da ima i oshthe edin chovek koito e choveka pod koito shte slojim noviq chovek, validachiq dali e svobodna pozichiqta
    private BinaryPerson addBinaryPerson(BinaryPerson binParent, BinaryPerson binChild ) {
        BinaryPerson child = binParent.getRightChild();
        if (binParent.isPreferredDirection()/*right*/) {
            if (Objects.isNull(child)) {
                binParent.setRightChild(binChild);
                binChild.setParent(binParent);
                binaryPersonRepository.save(binParent);
            } else {
                addBinaryPerson(child, binChild);
            }
        } else {
            if (Objects.isNull(binParent.getLeftChild())){
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

    @Override
    public void changePreferredDirection(BinaryPerson binaryPerson, boolean direction) {
        binaryPerson.setPreferredDirection(direction);
        binaryPersonRepository.save(binaryPerson);
    }
}
