package com.example.airlinereservation.dtos.response.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {
	private String message;
	private long flightDuration;
	private String arrivalAirportName;
	private String arrivalAirportCode;
	private String arrivalAirportAddress;
	private String departureAirportName;
	private String departureAirportCode;
	private String departureAirportAddress;
}
