package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.data.model.Airport;
import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.data.repositories.AirportRepository;
import com.example.airlinereservation.data.repositories.FlightRepository;
import com.example.airlinereservation.dtos.request.FlightRequest;
import com.example.airlinereservation.dtos.request.FlightUpdateRequest;
import com.example.airlinereservation.dtos.response.flight.FlightResponse;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.airlinereservation.utils.Constants.BOLA_AIR;

@Service
@AllArgsConstructor
public class BolaAir_FlightService implements FlightService{
	
	private FlightRepository flightRepository;
	private AirportRepository airportRepository;
	private ModelMapper mapper;
	private final Logger logger = LoggerFactory.getLogger(BolaAir_FlightService.class);
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public FlightResponse addFlight(FlightRequest flightRequest) throws InvalidRequestException {
		checkIfFlightExists(flightRequest.getArrivalCity(), flightRequest.getDepartureCity());
		try{
			Flight mappedFlight = mapper.map(flightRequest, Flight.class);
			Airport arrivalAirport = mapper.map(flightRequest.getArrivalAirportRequest(), Airport.class);
			Airport savedArrivalAirport = airportRepository.save(arrivalAirport);
			
			Airport departureAirport = mapper.map(flightRequest.getDepartureAirportRequest(), Airport.class);
			Airport savedDepartureAirport = airportRepository.save(departureAirport);
			
			mappedFlight.setArrivalAirport(savedArrivalAirport);
			mappedFlight.setDepartureAirport(savedDepartureAirport);
			mappedFlight.setAirline(BOLA_AIR);
			mappedFlight.setFlightInstances(new ArrayList<>());
			mappedFlight.setDurationInHrs(flightRequest.getEstimatedFlightDurationInMinutes());
			Flight savedFlight = flightRepository.save(mappedFlight);
			return buildFlightResponse(savedFlight);
		}
		catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException exception) {
			logger.error("{}", exception.getMessage());
			throw new InvalidRequestException(exception.getMessage(), exception);
		}
	}
	
	private void checkIfFlightExists(String arrivalCity, String departureCity) throws InvalidRequestException {
		boolean flightExists = flightRepository.existsByArrivalCityAndDepartureCity(arrivalCity, departureCity);
		if (flightExists)
			throw new InvalidRequestException("Flight Already Existed");
	}
	
	
	private FlightResponse buildFlightResponse(Flight savedFlight) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		FlightResponse response = FlightResponse.class.getDeclaredConstructor().newInstance();
		mapper.map(savedFlight, response);
		response.setArrivalAirportAddress(savedFlight.getArrivalAirport().getAddress());
		response.setArrivalAirportCode(savedFlight.getArrivalAirport().getIcaoCode());
		response.setArrivalAirportName(savedFlight.getArrivalAirport().getName());
		
		response.setDepartureAirportAddress(savedFlight.getDepartureAirport().getAddress());
		response.setDepartureAirportCode(savedFlight.getDepartureAirport().getIcaoCode());
		response.setDepartureAirportName(savedFlight.getDepartureAirport().getName());
		return response;
	}
	
	@Override
	public FlightResponse updateFlight(FlightUpdateRequest flightRequest) {
		return null;
	}
	
	@Override
	public Long getCountOfAllFlights() {
		return flightRepository.count();
	}
	
	@Override
	public List<Flight> getAllFLights() {
		return null;
	}
	
	@Override
	public FlightResponse getFlightByArrivalAndDepartureLocation(String arrivalCity, String departureCity) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvalidRequestException {
		Optional<Flight> foundFlight = flightRepository.findByLocation(arrivalCity, departureCity);
		if (foundFlight.isPresent())
			return buildFlightResponse(foundFlight.get());
		throw new InvalidRequestException("Flight Not Found");
	}
	
	@Override
	public void removeAll() {
		flightRepository.deleteAll();
		airportRepository.deleteAll();
	}
	
	
}
