package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.baseModule.MailSenderService;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Stack;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public void registerNewBinaryPerson(RegistrationPerson person, Long personToPutItOn, boolean preferredDirection) {
        addBinaryPerson(binParent(findSuitableParent(person)), person.getId(), personToPutItOn, preferredDirection);
    }

    @Override
    public void sendBinaryRegistrationEmail(RegistrationPerson registrationPerson, Long parentId) {
        try {
            mailSenderService.sendEmailWithoutAttachment(findSuitableParent(registrationPerson).getEmail(),
                    "Register a person in this binary tree:", "Click here to register him ->" +
                            "http://localhost:8080/register/binary/" + parentId);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public RegistrationPerson findSuitableParent(RegistrationPerson node) {
        Objects.requireNonNull(node, "nema node");
        RegistrationPerson parent = node.getParent();
        if (Objects.isNull(parent) || binaryPersonRepository.existsById(parent.getId())) {
            return parent;
        }
        return findSuitableParent(parent);
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

    public void addBinaryPerson(BinaryPerson binParent, Long childId, Long toqDetoMuGoTikatId, boolean preferredSide) {

        RegistrationPerson child = registrationPersonRepository.findById(childId).orElseThrow();
        BinaryPerson binChild = createBinaryPerson(child);

        if (Objects.isNull(binParent)) return;

        if (Objects.equals(binParent.getId(), toqDetoMuGoTikatId)) {
            if (preferredSide) {
                binParent.setRightChild(binChild);
            } else {
                binParent.setLeftChild(binChild);
            }
            binChild.setParent(binParent);
            System.out.println(binParent);
            binaryPersonRepository.save(binParent);
            binaryPersonRepository.save(binChild);
            return;
        }

        addBinaryPerson(binParent.getRightChild(), childId, toqDetoMuGoTikatId, preferredSide);
        addBinaryPerson(binParent.getLeftChild(), childId, toqDetoMuGoTikatId, preferredSide);
    }

//    public void addBinaryPerson(BinaryPerson binaryParent, Long childId, Long toqDetoMuGoTikatId, boolean preferredSide){
//        RegistrationPerson child = registrationPersonRepository.findById(childId).orElseThrow();
//        BinaryPerson binChild = createBinaryPerson(child);
//
//        if(Objects.isNull(binaryParent) || Objects.isNull(toqDetoMuGoTikatId)) return;
//
//        if (toqDetoMuGoTikatId.equals(binaryParent.getId()) || toqDetoMuGoTikatId.equals(childId)) return;
//
//        boolean isPersonInSubtree = false;
//        Stack<BinaryPerson> stack = new Stack<>();
//        stack.push(binaryParent);
//        while (!stack.isEmpty()) {
//            BinaryPerson curr = stack.pop();
//            if (curr.equals(binaryParent)) {
//                isPersonInSubtree = true;
//                break;
//            }
//            if (curr.getLeftChild() != null) {
//                stack.push(curr.getLeftChild());
//            }
//            if (curr.getRightChild() != null) {
//                stack.push(curr.getRightChild());
//            }
//        }
//        if (isPersonInSubtree) {
//            child.setName(person.getName());
//            child.setEmail(person.getEmail());
//        }
//    }

}
