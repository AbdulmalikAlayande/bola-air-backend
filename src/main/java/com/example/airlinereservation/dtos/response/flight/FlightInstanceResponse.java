package com.example.airlinereservation.dtos.response.flight;

import com.example.airlinereservation.dtos.response.AirportResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightInstanceResponse {
	
	private int baggageAllowance;
	private String flightNumber;
	private long flightDuration;
	private boolean isFullyBooked;
	private AirportResponse departureAirport;
	private AirportResponse arrivalAirport;
	private String departureDate;
	private String arrivalDate;
	private String departureTime;
	private String arrivalTime;
	
}
