package com.example.piramidadjii.security;

import com.example.piramidadjii.registrationTreeModule.entities.Authority;
import com.example.piramidadjii.registrationTreeModule.entities.RegistrationPerson;
import com.example.piramidadjii.registrationTreeModule.entities.Role;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RegistrationPersonRepository registrationPersonRepository;

    @Autowired
    public CustomUserDetailsService(RegistrationPersonRepository registrationPersonRepository) {
        this.registrationPersonRepository = registrationPersonRepository;
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(List<Authority> roles) {
        return roles.parallelStream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        RegistrationPerson registrationPerson = registrationPersonRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with " + email + "not found"));
        return new User(registrationPerson.getEmail(), registrationPerson.getPassword(), mapRolesToAuthorities(registrationPerson.getRole().getAuthorities()));
    }
}