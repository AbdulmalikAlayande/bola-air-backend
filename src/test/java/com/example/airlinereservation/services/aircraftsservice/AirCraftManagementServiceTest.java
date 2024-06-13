package com.example.airlinereservation.services.aircraftsservice;

import com.example.airlinereservation.data.model.aircraft.AirCraft;
import com.example.airlinereservation.data.model.enums.Destinations;
import com.example.airlinereservation.dtos.request.AirCraftRequest;
import com.example.airlinereservation.dtos.response.AirCraftResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
public class AirCraftManagementServiceTest {

	@Autowired
	AirCraftManagementService airAirCraftManagementService;
	AirCraftRequest airCraftRequest;
	AirCraftRequest airCraftRequest1;
	AirCraftRequest airCraftRequest2;
	AirCraftResponse response;
	@BeforeEach void startEachTestWith(){
		airCraftRequest = new AirCraftRequest();
		airCraftRequest.setAirCraftName("Airliner");
		airCraftRequest.setModel("Boeing 505");
		airCraftRequest.setLocation("Abuja");
		
		
		airCraftRequest1 = new AirCraftRequest();
		airCraftRequest1.setModel("AirBus 727");
		airCraftRequest1.setAirCraftName("Freighter");
		
		airCraftRequest2 = new AirCraftRequest();
		airCraftRequest2.setModel("Concorde");
		
		response = airAirCraftManagementService.addAircraftToHanger(airCraftRequest);
	}
	@Test void addAirCraftToHangerTest(){
		assertThat(response).isNotNull();
		assertThat(response.getAirCraftName()).isEqualTo(airCraftRequest.getAirCraftName());
		assertThat(airAirCraftManagementService.hangerContainsAirCraftByName(airCraftRequest.getAirCraftName())).isTrue();
		assertThat(airAirCraftManagementService.hangerContainsAirCraftByModel(airCraftRequest.getModel())).isTrue();
	}
	
	@Test void addSameAirCraftToHangerMultipleTimes_InvalidRequestExceptionIsThrown(){
	
	}
	
	@Test void removeAirCraftFromHangerTest(){
		AirCraft airCraft = new AirCraft();
		airCraft.setHangerId(airAirCraftManagementService.getTestHangerId());
		long countBefore = airAirCraftManagementService.getCountOfAirCraftInHanger();
		airAirCraftManagementService.removeAircraft(airCraft);
		long countAfter = airAirCraftManagementService.getCountOfAirCraftInHanger();
		assertThat(countBefore).isGreaterThan(countAfter);
		assertThat(airAirCraftManagementService.hangerContainsAirCraft(airCraft)).isFalse();
	}
	
	@Test void findAvailableAirCraftByLocationAndAvailabilityTest_AirCraftWhichIsAvailableIsReturned(){
		AirCraft foundAirCraft = airAirCraftManagementService.getAvailableAirCraft(Destinations.valueOf(airCraftRequest.getLocation().toUpperCase()), true);
		assertThat(foundAirCraft).isNotNull();
		assertThat(foundAirCraft.getLocation()).isEqualTo(Destinations.valueOf(airCraftRequest.getLocation().toUpperCase()));
		assertThat(foundAirCraft.isAvailable()).isTrue();
	}
	
	@Test void findAirCraftByModelTest_FoundAirCraftWithSaidModelIsReturned(){
		List<AirCraft> foundAirCraft = airAirCraftManagementService.getAirCraftByModel("Boeing 505");
		assertThat(foundAirCraft.size()).isGreaterThan(BigInteger.ZERO.intValue());
		assertThat(foundAirCraft.stream().findAny().isPresent()).isTrue();
	}
	
	@Test void findAirCraftByHangerIdThatExists_FoundAirCraftIsReturned(){
	
	}
	
	@Test void findAirCraftByModelThatDoesNotExist_ExceptionIsThrown(){
	
	}
}