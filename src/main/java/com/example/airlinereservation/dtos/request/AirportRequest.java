package com.example.airlinereservation.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class AirportRequest {
	
	private long longitude;
	private long latitude;
	private String icaoCode;
	private String iataCode;
	private String countryName;
	private String airportName;
}
