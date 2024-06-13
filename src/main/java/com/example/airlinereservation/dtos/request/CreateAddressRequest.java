package com.example.airlinereservation.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateAddressRequest {
	
	private String country;
	private String state;
	private String postalCode;
	private String streetName;
	private String streetNumber;
	private String houseNumber;
}
