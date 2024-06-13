package com.example.airlinereservation.dtos.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightRequest {
	
	private String airline;
	private String displayImageName;
	@NotBlank
	private long estimatedFlightDurationInMinutes;
	@NonNull
	@NotBlank
	private String arrivalCity;
	@NonNull
	@NotBlank
	private String departureCity;
	@NonNull
	private AirportRequest arrivalAirportRequest;
	@NonNull
	@NotBlank
	private AirportRequest departureAirportRequest;
	
}
