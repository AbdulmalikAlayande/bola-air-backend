package com.example.airlinereservation.dtos.request;

import com.example.airlinereservation.data.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightUpdateRequest {
	private String flightId;
	@Nullable private LocalTime departureTime;
	@Nullable private LocalDate departureDate;
	@Nullable private LocalTime arrivalTime;
	@Nullable private LocalDate arrivalDate;
	@Nullable private String Airline;
	private int baggageAllowance;
	@Nullable private String destination;
	@Nullable private Passenger[] passengers;
}
