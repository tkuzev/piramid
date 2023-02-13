package com.example.piramidadjii.services;

import com.example.piramidadjii.entities.Person;

public interface RegistrationTreeService {
    void createChild(Person child,Person parent);
}
