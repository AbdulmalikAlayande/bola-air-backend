package com.example.airlinereservation.dtos.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInvitationRequest {

	@NotBlank
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid Email: Email Must Match The Format Specified")
	@Valid
	private String email;
	private String phoneNumber;
	private String notificationPreference;
}
