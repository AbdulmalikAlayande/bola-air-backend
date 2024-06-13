package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.data.model.enums.FlightStatus;
import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.data.model.flight.FlightInstance;
import com.example.airlinereservation.data.repositories.FlightInstanceRepository;
import com.example.airlinereservation.data.repositories.FlightRepository;
import com.example.airlinereservation.dtos.request.CreateFlightInstanceRequest;
import com.example.airlinereservation.dtos.response.flight.FlightInstanceResponse;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static com.example.airlinereservation.data.model.enums.FlightStatus.SCHEDULED;
import static com.example.airlinereservation.utils.Constants.FLIGHT_DOES_NOT_EXIST;
import static com.example.airlinereservation.utils.Constants.SCHEDULED_UNFILLED_FLIGHT_INSTANCE_STILL_EXIST;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;

@Service
public class BolaAir_FlightInstanceService implements FlightInstanceService{
	
	private final FlightInstanceRepository flightInstanceRepository;
	private final FlightRepository flightRepository;
	private final ModelMapper mapper;
	private final Validator validator;
	private final Logger logger = LoggerFactory.getLogger(BolaAir_FlightInstanceService.class);
	
	public BolaAir_FlightInstanceService(FlightInstanceRepository flightInstanceRepository, FlightRepository flightRepository, ModelMapper mapper, Validator validator) {
		this.flightInstanceRepository = flightInstanceRepository;
		this.flightRepository = flightRepository;
		this.validator = validator;
		Converter<ZonedDateTime, String> dateConverter = context -> context.getSource() == null ? null : context.getSource().toLocalDate().toString();
		Converter<ZonedDateTime, String> timeConverter = context -> context.getSource() == null ? null : context.getSource().toLocalTime().toString();
		mapper.typeMap(FlightInstance.class, FlightInstanceResponse.class)
			  .addMappings(map -> map.using(timeConverter).map(FlightInstance::getArrivalTime, FlightInstanceResponse::setArrivalTime))
			  .addMappings(map -> map.using(timeConverter).map(FlightInstance::getDepartureTime, FlightInstanceResponse::setDepartureTime))
			  .addMappings(map -> map.using(dateConverter).map(FlightInstance::getArrivalTime, FlightInstanceResponse::setArrivalDate))
			  .addMappings(map -> map.using(dateConverter).map(FlightInstance::getDepartureTime, FlightInstanceResponse::setDepartureDate));
		this.mapper = mapper;
	}
	
	@Override
	@Transactional(rollbackFor = {Exception.class, SQLException.class})
	public FlightInstanceResponse createNewInstance(CreateFlightInstanceRequest request){
		try{
			Set<ConstraintViolation<CreateFlightInstanceRequest>> violations = validator.validate(request);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}
			checkIfAFlightStillExists(request);
			Optional<Flight> foundFlight = flightRepository.findByLocation(request.getArrivalCity(), request.getDepartureCity());
			return foundFlight.map(flight -> {
				FlightInstance flightInstance = mapper.map(request, FlightInstance.class);
				flightInstance.setStatus(SCHEDULED);
				flightInstance.setFlightNumber(generateFlightNumber());
				flightInstance.setFlightSeat(new ArrayList<>());
				FlightInstance instance = performSeparationTechnique(flightInstance, flight.getDurationInHrs());
				FlightInstance savedInstance = flightInstanceRepository.save(instance);
				flight.getFlightInstances().add(savedInstance);
				flightRepository.save(flight);
				FlightInstanceResponse response = mapper.map(savedInstance, FlightInstanceResponse.class);
				mapper.map(flight, response);
				logger.info("response ==::) {}", response);
				return response;
			}).orElseThrow(() -> new InvalidRequestException(FLIGHT_DOES_NOT_EXIST));
		} catch (ConstraintViolationException | InvalidRequestException exception){
			logger.error("{}", exception.getMessage());
			System.err.println(exception.getMessage());
			throw new RuntimeException(exception.getMessage(), exception);
		}
	}
	
	private void checkIfAFlightStillExists(CreateFlightInstanceRequest request) throws InvalidRequestException {
		Optional<Flight> foundFlight = flightRepository.findByLocation(request.getArrivalCity(), request.getDepartureCity());
		Boolean scheduledInstanceExists = foundFlight.map(flight -> flight.getFlightInstances()
				                                                          .stream()
				                                                          .anyMatch(instance -> instance.getStatus() == SCHEDULED))
				                                     .orElseThrow(() -> new InvalidRequestException(FLIGHT_DOES_NOT_EXIST));
		if (scheduledInstanceExists)
			throw new InvalidRequestException(SCHEDULED_UNFILLED_FLIGHT_INSTANCE_STILL_EXIST);
	}
	
	private String generateFlightNumber() {
		long currentTimeMillis = System.currentTimeMillis();
		String currentTimeStamp = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
				                          .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		long uniqueTimeStamp = Long.parseLong(currentTimeStamp) + currentTimeMillis;
		try{
			MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
			byte[] hashBytes = messageDigest.digest(String.valueOf(uniqueTimeStamp).getBytes());
			long hashValue = 0;
			for (int index = 0; index < Math.min(hashBytes.length, Long.BYTES); index++) {
				hashValue <<= 8;
				hashValue |= hashBytes[index] & 0xFF;
			}
			return "Flight-" + getRandomAlphabet() + hashValue;
		} catch (NoSuchAlgorithmException exception){
			logger.error("{}", exception.getMessage());
			System.err.println(exception.getMessage());
			return String.valueOf(currentTimeStamp.hashCode());
		}
	}
	
	private String getRandomAlphabet() {
		SecureRandom random = new SecureRandom();
		int randomAlphabetAsciiCode = random.nextInt(26) + 65;
		return Character.toString(randomAlphabetAsciiCode);
	}
	
	private FlightInstance performSeparationTechnique(FlightInstance mappedFlight, long estimatedTimeOfArrival) {
		Optional<FlightInstance> enRouteInstances = flightInstanceRepository.findLastMovedFlight(ZonedDateTime.now());
		enRouteInstances.ifPresentOrElse(instance -> {
					mappedFlight.setDepartureTime(instance.getDepartureTime().plusHours(BigInteger.valueOf(5).intValue()));
					mappedFlight.setArrivalTime(mappedFlight.getDepartureTime().plusHours(estimatedTimeOfArrival));
				},
				() -> {
					mappedFlight.setDepartureTime(ZonedDateTime.now().plusHours(BigInteger.valueOf(5).intValue()));
					mappedFlight.setArrivalTime(mappedFlight.getDepartureTime().plusHours(estimatedTimeOfArrival));
				}
				);
		return mappedFlight;
	}
	
	@Override
	public List<FlightInstanceResponse> findAllBy(FlightStatus status){
		List<FlightInstance> foundInstances = flightInstanceRepository.findByStatus(status);
		return foundInstances.stream().map(instance -> mapper.map(instance, FlightInstanceResponse.class)).toList();
	}
	
	@Override
	public FlightInstanceResponse findBy(ZonedDateTime departureTime, ZonedDateTime arrivalTime) {
		FlightInstance exampleInstance = FlightInstance.builder().departureTime(departureTime).arrivalTime(arrivalTime).build();
		Example<FlightInstance> example = Example.of(exampleInstance);
		Function<FluentQuery.FetchableFluentQuery<FlightInstance>, FlightInstanceResponse> queryFunction = query -> {
			Optional<FlightInstance> instance = query.stream()
					                          .filter(flightInstance -> flightInstance.getDepartureTime().equals(departureTime) &&
																	  flightInstance.getArrivalTime().equals(arrivalTime))
					                          .findFirst();
			return mapper.map(instance.orElseThrow(), FlightInstanceResponse.class);
		};
		return flightInstanceRepository.findBy(example, queryFunction);
	}
	
	@Override
	public void removeAll() {
		flightInstanceRepository.deleteAll();
	}
}

/*
TODO: 12/24/2023
 |==|)) If a flight instance still exist and it is neither filled nor en-route yet
 don't create a new one, meaning by arrival and departure location, by date and time
 |==|)) If a flight instance is to be created let it be spaced by at least 5hrs
 |==|)) To Make the component cleaner, just add nested DTOs to the DTOs that already existed, That is the only way
 
response.setDepartureDate(flightInstance.getDepartureTime().toLocalDate().toString());
response.setDepartureTime(flightInstance.getDepartureTime().toLocalTime().toString());
response.setArrivalDate(flightInstance.getArrivalTime().toLocalDate().toString());
response.setArrivalTime(flightInstance.getArrivalTime().toLocalTime().toString());
*/