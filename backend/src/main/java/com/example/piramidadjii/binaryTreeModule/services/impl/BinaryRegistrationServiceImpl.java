package com.example.piramidadjii.binaryTreeModule.services.impl;

import com.example.piramidadjii.baseModule.MailSenderService;
import com.example.piramidadjii.binaryTreeModule.dtos.BinaryRegistrationDTO;
import com.example.piramidadjii.binaryTreeModule.entities.BinaryPerson;
import com.example.piramidadjii.binaryTreeModule.repositories.BinaryPersonRepository;
import com.example.piramidadjii.binaryTreeModule.services.BinaryRegistrationService;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

@Service
public class BinaryRegistrationServiceImpl implements BinaryRegistrationService {
    @Autowired
    private BinaryPersonRepository binaryPersonRepository;
    @Autowired
    private RegistrationPersonRepository registrationPersonRepository;
    @Autowired
    private MailSenderService mailSenderService;

    private ModelMapper mapper;

    @Override
    public void registerNewBinaryPerson(RegistrationPerson person, Long personToPutItOn, boolean preferredDirection) {
        addBinaryPerson(binParent(findSuitableParent(person)), person.getId(), personToPutItOn, preferredDirection);
    }

    @Override
    public void sendBinaryRegistrationEmail(RegistrationPerson registrationPerson, Long parentId) {
        try {
            SecureRandom random=new SecureRandom();
            byte[] tokenBytes=new byte[16];
            random.nextBytes(tokenBytes);
            String verificationToken= Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
            mailSenderService.sendEmailWithoutAttachment(findSuitableParent(registrationPerson).getEmail(),
                    "Register a person in this binary tree:", "Click here to register him ->" +
                            "http://localhost:8080/register/binary/verify?token=" + verificationToken);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public List<BinaryRegistrationDTO> getAllKids(BinaryPerson binaryPerson) {
        List<BinaryRegistrationDTO> children = new ArrayList<>();
        if (binaryPerson.getLeftChild() != null) {
            BinaryRegistrationDTO mapped = mapper.map(binaryPerson.getLeftChild(), BinaryRegistrationDTO.class);
            children.add(mapped);
            children.addAll(getAllKids(binaryPerson.getLeftChild()));
        }
        if (binaryPerson.getRightChild() != null) {
            BinaryRegistrationDTO mapped = mapper.map(binaryPerson.getRightChild(), BinaryRegistrationDTO.class);
            children.add(mapped);
            children.addAll(getAllKids(binaryPerson.getRightChild()));
        }
        return children;
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
