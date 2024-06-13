package com.example.airlinereservation.services.flightformservice;

import com.example.airlinereservation.dtos.request.FlightFormRequest;
import com.example.airlinereservation.dtos.request.FlightRequest;
import com.example.airlinereservation.dtos.response.flight.FlightFormResponse;

import java.util.List;
import java.util.Optional;

public class FlightFormServiceImplementation implements FlightFormService{
	
	private static FlightFormService instance = null;
	
	public static FlightFormService getInstance() {
		if (instance == null)
			return new FlightFormServiceImplementation();
		return instance;
	}
	
	private FlightFormServiceImplementation(){}
	@Override
	public Optional<FlightFormResponse> generateFlightForm(FlightRequest flightRequest) {
		return Optional.empty();
	}
	
	@Override
	public Optional<FlightFormResponse> save(FlightFormRequest flightFormRequest) {
		return Optional.empty();
	}
	
	@Override
	public Optional<FlightFormResponse> findById(String flightFormId) {
		return Optional.empty();
	}
	
	@Override
	public String deleteFlightFormBy(String flightFormId) {
		return null;
	}
	
	@Override
	public Optional<List<FlightFormResponse>> findAll() {
		return Optional.empty();
	}
	
	@Override
	public Optional<List<FlightFormResponse>> findAllByPassengerId(String passengerId) {
		return Optional.empty();
	}
	
	@Override
	public Optional<List<FlightFormResponse>> findAllByFlightId(String flightId) {
		return Optional.empty();
	}
}
