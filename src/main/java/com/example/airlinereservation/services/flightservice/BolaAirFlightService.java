package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.data.model.Passenger;
import com.example.airlinereservation.data.model.aircraft.AirCraft;
import com.example.airlinereservation.data.model.enums.TravelClass;
import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.dtos.request.BookingRequest;
import com.example.airlinereservation.dtos.response.users.CustomerResponse;
import com.example.airlinereservation.dtos.response.flight.FlightResponse;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.services.categories.*;
import com.example.airlinereservation.services.userservice.CustomerService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static com.example.airlinereservation.utils.Constants.INVALID_DESTINATION;


@Service
@AllArgsConstructor
public class BolaAirFlightService implements Bookable {
	CustomerService passengerService;
	ModelMapper mapper;
	private final List<Flight> availableFlights = new ArrayList<>();
	
	private final List<BookingCategory> bookingCategories = List.of(
			FirstClassBookingCategory.getInstance(),
			BusinessClassBookingCategory.getInstance(),
			PremiumEconomyClassBookingCategory.getInstance(),
			EconomyClassBookingCategory.getInstance()
	);
	@Override
	public Flight getAvailableFlight(String destination) throws InvalidRequestException {
		try {
			return availableFlight(destination);
		}catch (Exception exception){
			throw new InvalidRequestException(INVALID_DESTINATION);
		}
	}
	
	@NotNull
	private Flight availableFlight(String destination) {
		return new Flight();
	}
	
	public FlightResponse checkAvailableFlight(String destination) throws InvalidRequestException {
		try {
			Flight flight = availableFlight(destination);
			FlightResponse response = new FlightResponse();
			mapper.map(flight, response);
			return response;
		}catch (Exception exception){
			throw new InvalidRequestException(INVALID_DESTINATION);
		}
	}
	
	@Override
	public FlightResponse bookFLight(BookingRequest bookingRequest) {
		return null;
	}
	
	@Override
	public FlightResponse getAvailableSeatsByFlightId(String flightId) {
		return null;
	}
	
	@Override
	public String cancelFlight(String passengerUsername) {
		return null;
	}
	
	@Override
	public boolean isNotFilled(Flight foundFlight) {
		return false;
	}
	
	@Override
	public Flight createNewFlight(String destination) {
		Flight flight = new Flight();
		AirCraft availableAirCraft = getAvailableAirCraft();
		return flight;
	}
	
	private AirCraft getAvailableAirCraft() {
		return null;
	}
	
	@Override
	public void assignSeatToPassenger(Passenger passenger, String destination, int category) {
	
	}
	
	private Flight newFlightReadyForBooking(Flight flight) {
		return null;
	}
	
	@Override
	public Flight bookFlight(BookingRequest bookingRequest) throws InvalidRequestException {
		if (bookingRequestIsValid(bookingRequest) && paymentIsCompleted(bookingRequest) && nameIsValid(bookingRequest)) {
			Flight availableFlight = getAvailableFlight("");
			setPassengerToArrayOfPassengers(bookingRequest, availableFlight);
			BookingCategory bookingCategory = getBookingCategory(bookingRequest.getBookingCategory());
			if (bookingCategory.canBook(availableFlight)) {
				bookingCategory.assignSeat(availableFlight);
				return availableFlight;
			}
		}
		throw new RuntimeException("Invalid booking request");
	}
	
	private void setPassengerToArrayOfPassengers(BookingRequest bookingRequest, Flight availableFlight) {
	
	}
	
	private Optional<Passenger> foundPassenger(BookingRequest bookingRequest) {
		return passengerService.findPassengerByUserNameForAdmin(bookingRequest.getPassengerUsername());
	}
	
	private BookingCategory getBookingCategory(int bookingCategory){
		return bookingCategories.get(bookingCategory);
	}
	
	private boolean paymentIsCompleted(BookingRequest  bookingRequest) {
		return true;
	}
	
	@SneakyThrows
	private boolean nameIsValid(BookingRequest bookingRequest){
		Optional<CustomerResponse> passengerResponse = passengerService.findCustomerByUserName(bookingRequest.getPassengerUsername());
		return passengerResponse.isPresent();
	}
	
	private boolean bookingRequestIsValid(BookingRequest bookingRequest) {
		boolean isInvalidBookingRequest = false;
		return !isInvalidBookingRequest && isValidTravelClass(bookingRequest);
	}
	
	private boolean isValidTravelClass(BookingRequest bookingRequest) {
		for (TravelClass travelClass : EnumSet.range(TravelClass.FIRST_CLASS, TravelClass.ECONOMY_CLASS))
			if (travelClass.ordinal() == bookingRequest.getBookingCategory()) {
				return true;
			}
		throw new IllegalArgumentException("Invalid booking category");
	}
	
	private Flight newFlightReadyForBooking() {
		return new Flight();
	}
}