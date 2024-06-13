package com.example.airlinereservation.services.aircraftsservice;

import com.example.airlinereservation.dtos.request.AirCraftRequest;
import com.example.airlinereservation.dtos.response.AirCraftResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BolaAir_AircraftService implements AirCraftService{
	@Override
	public AirCraftResponse saveAirCraft(AirCraftRequest airCraftRequest) {
		return null;
	}
	
	@Override
	public AirCraftResponse assignAircraft(AirCraftRequest airCraftRequest) {
		return null;
	}
}
