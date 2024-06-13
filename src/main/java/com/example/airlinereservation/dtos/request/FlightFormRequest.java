package com.example.airlinereservation.dtos.request;


import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.data.model.Passenger;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FlightFormRequest {
	private Passenger passenger;
	private Flight flight;
}
