package com.example.airlinereservation.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportResponse {
	
	private String name;
	private String icaoCode;
	private String iataCode;
	private String isoCountryCode;
	private String address;
	private Long longitude;
	private Long latitude;
}
