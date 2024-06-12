package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.data.model.enums.FlightStatus;
import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.data.model.flight.FlightInstance;
import com.example.airlinereservation.data.repositories.FlightInstanceRepository;
import com.example.airlinereservation.data.repositories.FlightRepository;
import com.example.airlinereservation.dtos.Request.CreateFlightInstanceRequest;
import com.example.airlinereservation.dtos.Response.FlightInstanceResponse;
import org.json.Property;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.example.airlinereservation.data.model.enums.FlightStatus.SCHEDULED;

@Service
public class BolaAir_FlightInstanceService implements FlightInstanceService{
	
	private final FlightInstanceRepository flightInstanceRepository;
	private final FlightRepository flightRepository;
	private final ModelMapper mapper;
	private final Logger logger = LoggerFactory.getLogger(BolaAir_FlightInstanceService.class);
	
	public BolaAir_FlightInstanceService(FlightInstanceRepository flightInstanceRepository, FlightRepository flightRepository, ModelMapper mapper){
		this.flightInstanceRepository = flightInstanceRepository;
		this.flightRepository = flightRepository;
		mapper.typeMap(CreateFlightInstanceRequest.class, FlightInstance.class)
			  .setConverter(context -> {
				  ZonedDateTime arrivalTime = ZonedDateTime.of(
						  LocalDate.parse(context.getSource().getArrivalDate()),
						  LocalTime.parse(context.getSource().getArrivalTime()),
						  ZoneId.of(context.getSource().getArrivalCityZone())
				  );
				  ZonedDateTime departureTime = ZonedDateTime.of(
						  LocalDate.parse(context.getSource().getDepartureDate()),
						  LocalTime.parse(context.getSource().getDepartureTime()),
						  ZoneId.of(context.getSource().getDepartureCityZone())
				  );
				  return FlightInstance.builder().departureTime(departureTime).arrivalTime(arrivalTime).build();
			  });
		mapper.typeMap(Flight.class, FlightInstanceResponse.class)
			  .addMappings(mapping -> {
				  mapping.map(src -> src.getArrivalAirport().getAirportName(), FlightInstanceResponse::setArrivalAirportName);
				  mapping.map(src -> src.getArrivalAirport().getIcaoCode(), FlightInstanceResponse::setArrivalAirportIcaoCode);
				  mapping.map(src -> src.getArrivalAirport().getAirportAddress(), FlightInstanceResponse::setArrivalAirportAddress);
				  mapping.map(src -> src.getDepartureAirport().getAirportName(), FlightInstanceResponse::setDepartureAirportName);
				  mapping.map(src -> src.getDepartureAirport().getIcaoCode(), FlightInstanceResponse::setDepartureAirportIcaoCode);
				  mapping.map(src -> src.getDepartureAirport().getAirportAddress(), FlightInstanceResponse::setDepartureAirportAddress);
			  });
		this.mapper = mapper;
	}
	
	@Override
	@Transactional(rollbackFor = {Exception.class, SQLException.class})
	public FlightInstanceResponse createNewInstance(CreateFlightInstanceRequest request){
		checkIfAFlightStillExists(request);
		Optional<Flight> foundFlight = flightRepository.findByLocation(request.getArrivalCity(), request.getDepartureCity());
		return foundFlight.map(flight -> {
			FlightInstance flightInstance = mapper.map(request, FlightInstance.class);
			flightInstance.setStatus(SCHEDULED);
			flightInstance.setFlightNumber(generateFlightNumber());
			flightInstance.setFlightSeat(new ArrayList<>());
			FlightInstance savedInstance = flightInstanceRepository.save(flightInstance);
			flight.getFlightInstances().add(savedInstance);
			flightRepository.save(flight);
			FlightInstanceResponse response = mapper.map(savedInstance, FlightInstanceResponse.class);
			mapper.map(flight, response);
			logger.info("{}", response);
			return response;
		}).orElseThrow();
	}
	
	private void checkIfAFlightStillExists(CreateFlightInstanceRequest request) {
	
	}
	
	private String generateFlightNumber() {
		long currentTimeMillis = System.currentTimeMillis();
		String currentTimeStamp = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
				                          .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		long uniqueTimeStamp = Long.parseLong(currentTimeStamp) + currentTimeMillis;
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
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
	
	private FlightInstance performSeparationTechnique(List<FlightInstance> enRouteInstances, FlightInstance mappedFlight) {
		return new FlightInstance();
	}
	
	@Override
	public List<FlightInstanceResponse> findAllBy(FlightStatus status){
		List<FlightInstance> foundInstances = flightInstanceRepository.findByStatus(status);
		return foundInstances.stream().map(this::flightInstanceResponse).toList();
	}
	
	private FlightInstanceResponse flightInstanceResponse(FlightInstance flightInstance) {
		FlightInstanceResponse response = mapper.map(flightInstance, FlightInstanceResponse.class);
		response.setDepartureDate(flightInstance.getDepartureTime().toLocalDate().toString());
		response.setDepartureTime(flightInstance.getDepartureTime().toLocalTime().toString());
		response.setArrivalDate(flightInstance.getArrivalTime().toLocalDate().toString());
		response.setArrivalTime(flightInstance.getArrivalTime().toLocalTime().toString());
		return response;
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
*/