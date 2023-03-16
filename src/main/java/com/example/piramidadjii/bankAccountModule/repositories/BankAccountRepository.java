package com.example.piramidadjii.bankAccountModule.repositories;

import com.example.piramidadjii.bankAccountModule.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

}
