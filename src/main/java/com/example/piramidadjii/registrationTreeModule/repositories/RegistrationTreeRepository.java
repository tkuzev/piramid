package com.example.piramidadjii.registrationTreeModule.repositories;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface RegistrationTreeRepository extends JpaRepository<RegistrationTree, Long> {

    Optional<RegistrationTree> getRegistrationTreeById(Long id);

    @Transactional
    @Modifying
    @Query("update RegistrationTree c set c.balance = ?1 where c.id = ?2")
    void updateBalance(BigDecimal balance, Long id);

}
