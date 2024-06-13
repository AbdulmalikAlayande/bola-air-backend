package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.data.model.Passenger;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.dtos.request.BookingRequest;
import com.example.airlinereservation.dtos.response.flight.FlightResponse;

public interface Bookable {
	Flight bookFlight(BookingRequest bookingRequest) throws InvalidRequestException;
	Flight getAvailableFlight(String destination) throws InvalidRequestException;
	boolean isNotFilled(Flight foundFlight);
	
	Flight createNewFlight(String abuja);
	void assignSeatToPassenger(Passenger passenger, String destination, int category);
	FlightResponse checkAvailableFlight(String destination) throws InvalidRequestException;
	FlightResponse bookFLight(BookingRequest bookingRequest);
	FlightResponse getAvailableSeatsByFlightId(String flightId);
	
	String cancelFlight(String passengerUsername);
}
