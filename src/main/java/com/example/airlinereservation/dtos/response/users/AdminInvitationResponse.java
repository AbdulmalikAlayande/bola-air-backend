package com.example.airlinereservation.dtos.response.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdminInvitationResponse {
	public String message;
	private String email;
	private String code;
}
