package com.example.piramidadjii;

import com.example.piramidadjii.registrationTreeModule.entities.RegistrationTree;
import com.example.piramidadjii.registrationTreeModule.repositories.RegistrationTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class PiramidadjiiApplication {

	@Autowired
	private RegistrationTreeRepository registrationTreeRepository;

	public static void main(String[] args) {
		SpringApplication.run(PiramidadjiiApplication.class, args);
	}


	@Scheduled(cron = "00 00 00 * * *", zone = "Europe/Sofia")
	public void getTax(){
		List<RegistrationTree> allBySubscriptionExpirationDateFalse =
				registrationTreeRepository.getAllBySubscriptionExpirationDate(LocalDate.now());

		allBySubscriptionExpirationDateFalse.forEach(this::getTax);
	}

	private void getTax(RegistrationTree registrationTree){

		BigDecimal newBalance=registrationTree.getBalance()
				.subtract(registrationTree.getSubscriptionPlan().getRegistrationFee());

		if (registrationTree.isSubscriptionEnabled() && newBalance.compareTo(BigDecimal.ZERO)>=0) {
			registrationTree.setBalance(newBalance);
			registrationTree.setSubscriptionExpirationDate(LocalDate.now().plusMonths(1));
			registrationTreeRepository.save(registrationTree);
		}else {
			registrationTree.setSubscriptionEnabled(false);
			registrationTreeRepository.save(registrationTree);
		}

	}

}
