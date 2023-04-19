package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.baseModule.MailSenderService;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.facade.exceptions.IdNotFoundException;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public void registerNewBinaryPerson(Long personId, Long personToPutItOn, boolean preferredDirection) {
        Optional<RegistrationPerson> person = registrationPersonRepository.findById(personId);
        if (person.isPresent()){
            addBinaryPerson(binParent(findSuitableParent(person.get())), person.get().getId(), personToPutItOn, preferredDirection);
        }else {
            throw new IdNotFoundException(personId);
        }
    }

    @Override
    public void sendBinaryRegistrationEmail(RegistrationPerson registrationPerson, Long parentId) {
        try {
            RegistrationPerson suitableParent = findSuitableParent(registrationPerson);
            mailSenderService.sendEmailWithoutAttachment(suitableParent.getEmail(),
                    "Register a person in this binary tree:", "Click here to register him ->" +
                            "http://localhost:4200/register/binary/"+suitableParent.getId()+"/"+registrationPerson.getId());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Map<BinaryPerson, Boolean> getTree(Long binaryPersonId) {
        Optional<BinaryPerson> binaryPerson = binaryPersonRepository.findById(binaryPersonId);
        if (binaryPerson.isPresent()){
            Map<BinaryPerson, Boolean> tree = new HashMap<>();
            if (Objects.isNull(binaryPerson.get().getLeftChild()) || Objects.isNull(binaryPerson.get().getRightChild())) {
                tree.put(binaryPerson.get(), null);
            }
            traverseHelper(binaryPerson.get().getLeftChild(), tree, false);
            traverseHelper(binaryPerson.get().getRightChild(), tree, true);
            return tree;
        }else {
            throw new IdNotFoundException(binaryPersonId);
        }
    }

    @Override
    public BinaryPerson getBinaryById(Long id) {
        Optional<BinaryPerson> binaryPerson = binaryPersonRepository.findById(id);
        if (binaryPerson.isPresent()){
            return binaryPerson.get();
        }
        throw new IdNotFoundException(id);
    }


    private void traverseHelper(BinaryPerson binaryPerson, Map<BinaryPerson, Boolean> tree, Boolean direction) {
        if (Objects.isNull(binaryPerson)) return;


        if (Objects.isNull(binaryPerson.getRightChild()) || Objects.isNull(binaryPerson.getLeftChild())) {
            tree.put(binaryPerson, direction);
        }

        traverseHelper(binaryPerson.getLeftChild(), tree, direction);
        traverseHelper(binaryPerson.getRightChild(), tree, direction);
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

    @Transactional
    public void addBinaryPerson(BinaryPerson binaryParent, Long childId, Long toqDetoMuGoTikatId, boolean preferredSide) {
        RegistrationPerson child = registrationPersonRepository.findById(childId).orElseThrow();
        BinaryPerson binChild = createBinaryPerson(child);
        BinaryPerson toqDetoMuGoTikat = binaryPersonRepository.findById(toqDetoMuGoTikatId).orElseThrow();

        if (Objects.isNull(binaryParent)) return;

        if (toqDetoMuGoTikatId.equals(binaryParent.getId())) {
            setProperties(binaryParent, preferredSide, binChild, toqDetoMuGoTikat);
            return;
        }

        boolean isPersonInSubtree = false;
        Stack<BinaryPerson> stack = new Stack<>();
        stack.push(binaryParent);
        while (!stack.isEmpty()) {
            BinaryPerson curr = stack.pop();
            if (curr.equals(binaryParent)) {
                isPersonInSubtree = true;
                break;
            }
            if (curr.getLeftChild() != null) {
                stack.push(curr.getLeftChild());
            }
            if (curr.getRightChild() != null) {
                stack.push(curr.getRightChild());
            }
        }
        if (isPersonInSubtree) {
            setProperties(binaryParent, preferredSide, binChild, toqDetoMuGoTikat);
        }
    }

    private void setProperties(BinaryPerson binaryParent, boolean preferredSide, BinaryPerson binChild, BinaryPerson toqDetoMuGoTikat) {
        if (preferredSide) {
            if (Objects.nonNull(toqDetoMuGoTikat.getRightChild()))
                throw new RuntimeException("choveka deto e podaden mu e zaeta taq pozichiq");
            toqDetoMuGoTikat.setRightChild(binChild);
        } else {
            if (Objects.nonNull(toqDetoMuGoTikat.getLeftChild()))
                throw new RuntimeException("choveka deto e podaden mu e zaeta taq pozichiq");
            toqDetoMuGoTikat.setLeftChild(binChild);
        }
        binChild.setParent(toqDetoMuGoTikat);
        binaryPersonRepository.save(binaryParent);
        binaryPersonRepository.save(binChild);
    }
}
