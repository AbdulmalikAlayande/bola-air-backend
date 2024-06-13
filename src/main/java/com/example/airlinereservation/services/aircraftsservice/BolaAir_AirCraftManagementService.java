package com.example.airlinereservation.services.aircraftsservice;

import com.example.airlinereservation.data.model.aircraft.AirCraft;
import com.example.airlinereservation.data.model.enums.Destinations;
import com.example.airlinereservation.dtos.request.AirCraftRequest;
import com.example.airlinereservation.dtos.response.AirCraftResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class BolaAir_AirCraftManagementService implements AirCraftManagementService{
	private final Set<AirCraft> hanger = new HashSet<>();

	private ModelMapper mapper;
	public UUID testHangerId;
	
	// FIXME: 8/13/2023 the code does not check for null fields
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
	public AirCraft getAvailableAirCraft(Destinations location, boolean availability) {
		for (AirCraft aircraft: hanger) {
			if (aircraft.isAvailable() && aircraft.getLocation().equals(location))
				return aircraft;
		}
		return null;
	}
	
	@Override
	public void removeAircraft(AirCraft aircraft) {
		hanger.removeIf(craft -> craft.equals(aircraft));
	}
	
	@Override
	public boolean hangerContainsAirCraftByName(String airCraftName) {
		return hanger.stream().anyMatch(airCraft -> Objects.equals(airCraft.getAirCraftName(), airCraftName));
	}
	
	@Override
	public List<AirCraft> getAirCraftByModel(String model) {
		List<AirCraft> foundAirCrafts = new ArrayList<>();
		hanger.forEach(airCraft -> {
			if (Objects.equals(airCraft.getModel(), model))
				foundAirCrafts.add(airCraft);
		});
		return foundAirCrafts;
	}
	
	@Override
	public boolean hangerContainsAirCraftByModel(String model) {
		return hanger.stream().anyMatch(airCraft -> Objects.equals(airCraft.getModel(), model));
	}
	
	@Override
	public boolean hangerContainsAirCraft(AirCraft airCraft) {
		return hanger.stream().anyMatch(craft -> craft.equals(airCraft));
	}

	@Override
	public UUID getTestHangerId(){
		return testHangerId;
	}
	
	@Override
	public long getCountOfAirCraftInHanger() {
		return hanger.size();
	}
}
