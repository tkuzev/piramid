package com.example.piramidadjii.binaryTreeModule.services.impl;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void registerNewBinaryPerson(RegistrationPerson person,BinaryPerson daTiGoNatikam, boolean preferredDirection) {
         addBinaryPerson(binParent(findSuitableParent(person)), createBinaryPerson(person),daTiGoNatikam,preferredDirection);
    }

    public RegistrationPerson findSuitableParent(RegistrationPerson node) {
        Objects.requireNonNull(node,"nema node");
        RegistrationPerson parent = node.getParent();

        return (Objects.isNull(/*root*/parent.getParent())) ? parent: findSuitableParent(parent);
    }
    private BinaryPerson createBinaryPerson(RegistrationPerson person) {
        BinaryPerson binPerson = new BinaryPerson(person.getId(), BigDecimal.ZERO, BigDecimal.ZERO);
        binPerson.setName(person.getName());
        binaryPersonRepository.save(binPerson);
        return binPerson;
    }
    private BinaryPerson binParent(RegistrationPerson parent) {
        return binaryPersonRepository.findById(parent.getId()).orElseThrow();
    }

    //todo da ima i oshthe edin chovek koito e choveka pod koito shte slojim noviq chovek, validachiq dali e svobodna pozichiqta
    private void addBinaryPerson(BinaryPerson binParent, BinaryPerson binChild, BinaryPerson toqDetoMuGoTikat, boolean preferredSide ) {

        if(Objects.isNull(binParent)) throw new RuntimeException("otide za chigari bashtata");

        if(binParent == toqDetoMuGoTikat){
            if (preferredSide) {
                binParent.setRightChild(binChild);
            }
            else {
                binParent.setLeftChild(binChild);
            }
            binChild.setParent(binParent);
            binaryPersonRepository.save(binParent);
            return;
        }
        addBinaryPerson(binParent.getRightChild(), binChild, toqDetoMuGoTikat, preferredSide);
        addBinaryPerson(binParent.getLeftChild(), binChild, toqDetoMuGoTikat, preferredSide);
    }
}
