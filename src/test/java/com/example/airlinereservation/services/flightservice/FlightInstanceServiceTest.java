package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.data.model.enums.FlightStatus;
import com.example.airlinereservation.dtos.Request.AirportRequest;
import com.example.airlinereservation.dtos.Request.CreateFlightInstanceRequest;
import com.example.airlinereservation.dtos.Request.FlightRequest;
import com.example.airlinereservation.dtos.Response.FlightInstanceResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
class FlightInstanceServiceTest {
	
	@Autowired
	private FlightInstanceService flightInstanceService;
	@Autowired
	private FlightService flightService;
	private FlightInstanceResponse response;
	private static final int ZERO = BigInteger.ZERO.intValue();
	
	@BeforeEach
	@SneakyThrows
	void startEachTestWith() {
		flightInstanceService.removeAll();
		flightService.removeAll();
		flightService.addFlight(buildFlight());
		response = flightInstanceService.createNewInstance(buildInstance());
		
	}
	
	@Test
	@SneakyThrows
	public void createNewFlightInstance_NewFlightIsCreatedTest(){
		assertThat(response).isNotNull();
		assertThat(flightInstanceService.findAllBy(FlightStatus.SCHEDULED).size()).isGreaterThan(ZERO);
		assertThat(flightInstanceService.findBy(
				ZonedDateTime.of(
					LocalDate.of(2024, 7, 6),
					LocalTime.of(7, 0, 0),
					ZoneId.of("Africa/Lagos")
				),
				ZonedDateTime.of(
					LocalDate.of(2024, 7, 6),
					LocalTime.of(18, 0, 0),
					ZoneId.of("Africa/Accra")
				)
		)).isNotNull();
	}
	
	@Test
	public void createNewFlightInstance_ProperFlightSpacingIsApplied_ToMaintainSafeDistanceBetweenConsecutiveFlights(){
	
	}
	
	@Test void createNewFlightInstance_AssignAircraftToFlightInstanceTest(){
	
	}
	@Test void createNewFlightInstance_AssignAircraftToFlightInstance_AircraftIsAssignedIfPassedFlightRequirement(){
	
	}
	
	@Test public void testThatIfFlightIsFilled_FlightMovementIsScheduledImmediately(){
	
	}
	
	private CreateFlightInstanceRequest buildInstance() {
		return CreateFlightInstanceRequest.builder()
				       .departureCity("Abuja")
				       .departureCityZone("Africa/Lagos")
				       .departureTime("07:00:00")
				       .departureDate("2024-07-06")
				       .arrivalCity("Accra")
				       .arrivalCityZone("Africa/Accra")
				       .arrivalTime("18:00:00")
				       .arrivalDate("2024-07-06")
				       .build();
	}
	
	private FlightRequest buildFlight() {
		return FlightRequest.builder()
				       .estimatedFlightDurationInMinutes(360)
				       .departureAirportRequest(
							   buildAirportRequest("Nnamdi Azikiwe International  Airport", "Nigeria", "3456", "45678")
				       )
				       .arrivalAirportRequest(
							   buildAirportRequest("Kotoka International Airport", "Ghana", "4598", "0237")
				       )
				       .arrivalCity("Accra")
				       .departureCity("Abuja")
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