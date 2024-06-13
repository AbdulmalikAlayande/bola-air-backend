package com.example.airlinereservation.services.notifications.mail;

import com.example.airlinereservation.dtos.request.NotificationRequest;
import com.example.airlinereservation.dtos.response.NotificationResponse;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import org.springframework.http.ResponseEntity;

public interface  MailService {
	
	ResponseEntity<NotificationResponse> importContacts(NotificationRequest notificationRequest);
	ResponseEntity<NotificationResponse> sendReservationConfirmationEmail(NotificationRequest notificationRequest);
	ResponseEntity<NotificationResponse> sendAdminInvitationEmail(NotificationRequest notificationRequest) throws InvalidRequestException;
	
	ResponseEntity<NotificationResponse> sendOtp(NotificationRequest notificationRequest) throws InvalidRequestException;
	
	ResponseEntity<NotificationResponse> sendFlightFormAsPdf(NotificationRequest notificationRequest);
}
