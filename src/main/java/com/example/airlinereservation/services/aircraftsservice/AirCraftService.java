package com.example.airlinereservation.services.aircraftsservice;

import com.example.airlinereservation.dtos.request.AirCraftRequest;
import com.example.airlinereservation.dtos.response.AirCraftResponse;

public interface AirCraftService {
	
	AirCraftResponse saveAirCraft(AirCraftRequest airCraftRequest);
	AirCraftResponse assignAircraft(AirCraftRequest airCraftRequest);
	
}
