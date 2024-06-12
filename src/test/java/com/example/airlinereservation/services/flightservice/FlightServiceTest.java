package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.dtos.Request.AirportRequest;
import com.example.airlinereservation.dtos.Request.FlightRequest;
import com.example.airlinereservation.dtos.Response.FlightResponse;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.math.BigInteger.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class FlightServiceTest {
	
	@Autowired
	private FlightService flightService;
	FlightResponse savedFlightResponse;
	@BeforeEach
	@SneakyThrows
	public void startEachTestWith(){
		flightService.removeAll();
		savedFlightResponse = flightService.addFlight(buildFlightRequest());
	}
	
	@Test
	@SneakyThrows
	public void createNewFlight_NewFlightIsAddedToAirlinesListOfFlightsTest(){
		assertThat(flightService.getCountOfAllFlights()).isGreaterThan(ZERO.longValue());
		assertThat(savedFlightResponse).isNotNull();
	}
	
	@Test
	@SneakyThrows
	void saveSameFlightTwice_testThat_SameFlightCannotExistInTheDatabase_By_ArrivalLocationAndDepartureLocation(){
		assertThatThrownBy(()->flightService.addFlight(buildFlightRequest()))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("Flight Already Existed");
	}
	@Test
	public void createFlightWithEmptyRequiredFields_ExceptionIsThrown(){
		assertThatThrownBy(()->flightService.addFlight(buildIncompleteFlight())).isInstanceOf(NullPointerException.class);
	}
	
	@Test
	@SneakyThrows
	public void createNewFlight_getCreatedFlightBy_ArrivalDestinationAndDepartureDestination(){
		FlightResponse foundFlight = flightService.getFlightByArrivalAndDepartureLocation("Lagos, Nigeria", "Abuja, Nigeria");
		System.out.println(foundFlight);
		assertThat(foundFlight).isNotNull();
	}
	
	private FlightRequest buildIncompleteFlight() {
		return FlightRequest.builder()
				       .estimatedFlightDurationInMinutes(3L)
				       .arrivalCity("China")
				       .build();
	}
	
	private FlightRequest buildFlightRequest() {
		return FlightRequest.builder()
				       .estimatedFlightDurationInMinutes(3L)
				       .arrivalCity("Lagos, Nigeria")
				       .departureCity("Abuja, Nigeria")
				       .arrivalAirportRequest(buildAirportRequest("Murtala Muhammed Airport", "Nigeria", "23456", "12345"))
				       .departureAirportRequest(buildAirportRequest("Nnamdi Azikwe International Airport", "Nigeria", "45632", "12345"))
				       .build();
	}
	
	public AirportRequest buildAirportRequest(String name, String country, String icaoCode, String iataCode){
		return AirportRequest.builder()
				       .airportName(name)
				       .countryName(country)
				       .icaoCode(icaoCode)
				       .iataCode(iataCode)
				       .longitude(-34567)
				       .latitude(45678)
				       .build();
	}
}
