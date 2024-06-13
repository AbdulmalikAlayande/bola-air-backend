package com.example.airlinereservation.dtos.response.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
	private String message;
	private String email;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String gender;
	private long otp;
}
