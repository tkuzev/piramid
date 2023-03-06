package com.example.piramidadjii.orchestraModule.impl;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.configModule.ConfigurationService;
import com.example.piramidadjii.orchestraModule.OrchestraService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Objects;

@Service
public class OrchestraServiceImpl implements OrchestraService {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;

    private ConfigurationService configurationService;

    @Override
    public BinaryPerson registerNewPerson(RegistrationPerson person, boolean preferredDirection) {
        return addBinaryPerson(binParent(findSuitableParent(person)), createBinaryPerson(person, preferredDirection));
    }

    public RegistrationPerson findSuitableParent(RegistrationPerson node) {
        Objects.requireNonNull(node,"nema node");
        RegistrationPerson parent = node.getParent();
        if (Objects.isNull(/*root*/parent.getParent()) || configurationService.isEligable(parent.getSubscriptionPlan())) {
            return parent;
        }
        else {
            return findSuitableParent(parent);
        }
    }
    private BinaryPerson createBinaryPerson(RegistrationPerson person, boolean preferredDirection) {
        BinaryPerson binPerson = new BinaryPerson();
        binPerson.setId(person.getId());
        binPerson.setBankAccount(person.getBankAccount());
        binPerson.setName(person.getName());
        binPerson.setEmail(person.getEmail());
        binPerson.setRightContainer(BigDecimal.ZERO);
        binPerson.setLeftContainer(BigDecimal.ZERO);
        binPerson.setPreferredDirection(preferredDirection);
        binaryPersonRepository.save(binPerson);
        return binPerson;
    }
    private BinaryPerson binParent(RegistrationPerson parent) {
        return binaryPersonRepository.findById(parent.getId()).orElseThrow();
    }

    private BinaryPerson addBinaryPerson(BinaryPerson binParent, BinaryPerson binChild) {
        if (binParent.isPreferredDirection()/*right*/) {
            if (Objects.isNull(binParent.getRightChild())) {
                binParent.setRightChild(binChild);
                binChild.setParent(binParent);
                binaryPersonRepository.save(binParent);
            } else {
                addBinaryPerson(binParent.getRightChild(), binChild);
            }
        } else {
            if (Objects.isNull(binParent.getLeftChild())) {
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
}
