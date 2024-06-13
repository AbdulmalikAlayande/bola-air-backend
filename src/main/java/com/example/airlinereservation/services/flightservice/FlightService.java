package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.dtos.request.FlightUpdateRequest;
import com.example.airlinereservation.dtos.response.flight.FlightResponse;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.dtos.request.FlightRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface FlightService {
	
	FlightResponse addFlight(FlightRequest flightRequest) throws InvalidRequestException;
	FlightResponse updateFlight(FlightUpdateRequest flightRequest);
	List<Flight> getAllFLights();
	FlightResponse getFlightByArrivalAndDepartureLocation(String arrivalCity, String departureCity) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvalidRequestException;
	Long getCountOfAllFlights();
	
	void removeAll();
}
