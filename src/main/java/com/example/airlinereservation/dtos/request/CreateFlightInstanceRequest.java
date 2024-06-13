package com.example.airlinereservation.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.airlinereservation.utils.Constants.FIELD_CANNOT_BE_EMPTY;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFlightInstanceRequest {
	/**
	 Can be in either UTC, GMT or Time Zone identifier format
	**/
	@NotEmpty(message = FIELD_CANNOT_BE_EMPTY)
	private String arrivalCityZone;
	/**
	 Can be in either UTC, GMT or Time Zone Identifier format
	**/
	@NotEmpty(message = FIELD_CANNOT_BE_EMPTY)
	private String departureCityZone;
//	@NotEmpty(message = FIELD_CANNOT_BE_EMPTY)
//	private String departureTime;
//	@NotEmpty(message = FIELD_CANNOT_BE_EMPTY)
//	private String arrivalTime;
//	@NotEmpty(message = FIELD_CANNOT_BE_EMPTY)
//	private String arrivalDate;
//	@NotEmpty(message = FIELD_CANNOT_BE_EMPTY)
//	private String departureDate;
	@NotEmpty(message = FIELD_CANNOT_BE_EMPTY)
	private String arrivalCity;
	@NotEmpty(message = FIELD_CANNOT_BE_EMPTY)
	private String departureCity;
	
}
