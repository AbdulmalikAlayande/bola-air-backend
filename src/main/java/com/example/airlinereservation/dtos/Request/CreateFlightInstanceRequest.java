package com.example.airlinereservation.dtos.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFlightInstanceRequest {
	/**
	 Can be in either UTC, GMT or Time Zone Identifier format
	**/
	@NotEmpty
	private String arrivalCityZone;
	/**
	 Can be in either UTC, GMT or Time Zone Identifier format
	**/
	@NotEmpty
	private String departureCityZone;
	@NotEmpty
	private String departureTime;
	@NotEmpty
	private String arrivalTime;
	@NotEmpty
	private String arrivalDate;
	@NotEmpty
	private String departureDate;
	@NotEmpty
	private String arrivalCity;
	@NotEmpty
	private String departureCity;
	
}
