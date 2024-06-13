package com.example.airlinereservation.services.notifications.mail;

import com.example.airlinereservation.config.EmailValidationConfig;
import com.example.airlinereservation.dtos.request.NotificationRequest;
import com.example.airlinereservation.dtos.response.NotificationResponse;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.airlinereservation.utils.Constants.*;

@Slf4j
@Service
@AllArgsConstructor
public class Mailer implements MailService{

	private final EmailValidationConfig validationConfig;
	private final RestTemplate restTemplate;
	private final TemplateEngine templateEngine;
	private final Context context;
	
	
	@NotNull
	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(API_KEY, validationConfig.getBrevoApiKey());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
	@NotNull
	private static Map<String, Object> getContextVariables(NotificationRequest notificationRequest) {
		Map<String, Object> contextVariables = new HashMap<>();
		contextVariables.put("firstName", notificationRequest.getFirstName());
		contextVariables.put("email", notificationRequest.getEmail());
		return contextVariables;
	}
	
	private Notification buildNotification(String htmlContent, String userEmail){
		Recipient recipient = Recipient.builder().email(userEmail).build();
		return Notification.builder()
				       .subject(ACCOUNT_ACTIVATION_MAIL_SUBJECT)
				       .sender(Sender.builder().name(SENDER_FULL_NAME).email(SENDER_EMAIL).build())
				       .recipients(Collections.singletonList(recipient))
				       .htmlContent(htmlContent)
				       .build();
	}
	
	@NotNull
	private ResponseEntity<NotificationResponse> sendNotification(NotificationRequest notificationRequest, String email) throws InvalidRequestException {
		HttpEntity<Notification> requestEntity = new HttpEntity<>(buildNotification(email, notificationRequest.getEmail()), getHttpHeaders());
		ResponseEntity<NotificationResponse> response = restTemplate.postForEntity(
				BREVO_SEND_EMAIL_API_URL,
				requestEntity,
				NotificationResponse.class
		);
		if (response.getStatusCode().is2xxSuccessful()){
			log.info("{} response body:: {}", MESSAGE_SUCCESSFULLY_SENT, Objects.requireNonNull(response.getBody()));
			return response;
		}
		else {
			log.error("{} response body:: {}", MESSAGE_FAILED_TO_SEND, Objects.requireNonNull(response.getBody()));
			throw new InvalidRequestException("Error " + response.getStatusCode());
		}
	}
	
	@Override
	public ResponseEntity<NotificationResponse> importContacts(NotificationRequest notificationRequest) {
		HttpEntity<NotificationRequest> request = new HttpEntity<>(notificationRequest, getHttpHeaders());
		return restTemplate.postForEntity(BREVO_CONTACTS_IMPORT_URL, request, NotificationResponse.class);
	}
	
	@Override
	public ResponseEntity<NotificationResponse> sendAdminInvitationEmail(NotificationRequest notificationRequest) throws InvalidRequestException {
		context.setVariable("firstName", notificationRequest.getFirstName());
		context.setVariable("email", notificationRequest.getEmail());
		context.setVariable("code", notificationRequest.getCode());
		context.setVariable("adminSignUpUrl", FRONTEND_BASE_URL+"admin-signup");
		String email = templateEngine.process(notificationRequest.getMailPath(), context);
		System.out.println(email);
		return sendNotification(notificationRequest, email);
	}
	
	@Override
	public ResponseEntity<NotificationResponse> sendOtp(NotificationRequest notificationRequest) throws InvalidRequestException {
		context.setVariable("firstName", notificationRequest.getFirstName());
		context.setVariable("email", notificationRequest.getEmail());
		context.setVariable("code", notificationRequest.getCode());
		String email = templateEngine.process(notificationRequest.getMailPath(), context);
		System.out.println(email);
		return sendNotification(notificationRequest, email);
	}
	
	@Override
	public ResponseEntity<NotificationResponse> sendFlightFormAsPdf(NotificationRequest notificationRequest) {
		
		return null;
	}
	
	@Override
	public ResponseEntity<NotificationResponse> sendReservationConfirmationEmail(NotificationRequest notificationRequest) {
		return null;
	}
}
