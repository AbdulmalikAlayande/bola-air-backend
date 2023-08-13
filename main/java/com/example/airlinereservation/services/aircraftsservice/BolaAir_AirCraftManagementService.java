package com.example.airlinereservation.services.aircraftsservice;

import com.example.airlinereservation.data.model.aircraft.AirCraft;
import com.example.airlinereservation.dtos.Request.AirCraftRequest;
import com.example.airlinereservation.dtos.Response.AirCraftResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class BolaAir_AirCraftManagementService implements AirCraftManagementService{
	private final Set<AirCraft> hanger = new HashSet<>();
<<<<<<< HEAD

=======
>>>>>>> 79c38e9e000117c65c009531b461a5765359ed42
	private ModelMapper mapper;
	public UUID testHangerId;
	
	// FIXME: 8/13/2023 the code does not check for null fields
	// TODO: 8/13/2023 write implementation to check for null fields and also check for duplicates
	@Override
	public AirCraftResponse addAircraftToHanger(AirCraftRequest airCraftRequest) {
		AirCraft airCraft = new AirCraft();
		mapper.map(airCraftRequest, airCraft);
		airCraft.setAvailable(true);
		testHangerId = airCraft.getHangerId();
		hanger.add(airCraft);
		AirCraftResponse airCraftResponse = new AirCraftResponse();
		mapper.map(airCraft, airCraftResponse);
		return airCraftResponse;
	}
	
	@Override
	public AirCraft getAvailableAirCraft(String location, boolean availability) {
		return null;
	}
	
	@Override
	public void removeAircraft(AirCraft aircraft) {
	
	}
	
	@Override
	public boolean hangerContainsAirCraftByName(String airCraftName) {
		return hanger.stream().anyMatch(airCraft -> Objects.equals(airCraft.getAirCraftName(), airCraftName));
	}
	
	@Override
	public boolean hangerContainsAirCraftByModel(String model) {
		return hanger.stream().anyMatch(airCraft -> Objects.equals(airCraft.getModel(), model));
	}
	
	@Override
	public boolean hangerContainsAirCraft(AirCraft airCraft) {
		return hanger.stream().anyMatch(craft -> craft.equals(airCraft));
	}
	
	public UUID getTestHangerId(){
		return testHangerId;
	}
}
