package com.example.airlinereservation.services.notifications;

import com.example.airlinereservation.data.model.notifications.Recipients;
import com.example.airlinereservation.dtos.request.NotificationRequest;
import com.example.airlinereservation.dtos.response.NotificationResponse;
import com.example.airlinereservation.services.notifications.mail.MailService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MailServiceTest {
	
	@Autowired
	private MailService mailService;
	private NotificationRequest notificationRequest;
	
	@BeforeEach void beforeEachTestStartWith(){
		Recipients recipient = new Recipients();
		recipient.setEmail("alayandezainab64@gmail.com");
		notificationRequest = new NotificationRequest();
	}
	
	@SneakyThrows
	@Test void sendEmailTest(){
	
		ResponseEntity<NotificationResponse> response = mailService.sendAdminInvitationEmail(notificationRequest);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
	}
	
	@Test void testThatEmailContainsHtmlContent(){
	
	}
	
	@Test void importContactTest(){
		mailService.importContacts(notificationRequest);
	}
}
