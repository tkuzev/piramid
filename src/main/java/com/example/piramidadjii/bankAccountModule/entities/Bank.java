package com.example.piramidadjii.bankAccountModule.entities;

import com.example.piramidadjii.baseModule.baseEntites.BaseEntity;
import com.example.piramidadjii.baseModule.enums.Description;
import com.example.piramidadjii.baseModule.enums.OperationType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank extends BaseEntity {
    private BigDecimal amount;
    @ManyToOne
    private BankAccount srcAccId;
    @ManyToOne
    private BankAccount dstAccId;
    private BigDecimal itemPrice;
    private Long level;
    private Long percent;
    @Enumerated(EnumType.STRING)
    private Description description;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private LocalDateTime transactionDate;
}
