package com.example.airlinereservation.Controller;


import com.example.airlinereservation.dtos.request.CreateFlightInstanceRequest;
import com.example.airlinereservation.dtos.response.ApiResponse;
import com.example.airlinereservation.dtos.response.flight.FlightInstanceResponse;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.services.flightservice.FlightInstanceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("bola-air/")
@CrossOrigin("*")
@AllArgsConstructor
public class BolaAir_FlightInstanceController {
	
	private final FlightInstanceService flightInstanceService;
	@GetMapping("available-flights/")
	public ApiResponse<?> getAvailableFlights(){
		return null;
	}
	
	@PostMapping("create-new-flight-instance")
	public ApiResponse<?> createNewInstance(@Valid @RequestBody CreateFlightInstanceRequest instanceRequest){
		try {
			ApiResponse<FlightInstanceResponse> apiResponse = new ApiResponse<>();
			FlightInstanceResponse response = flightInstanceService.createNewInstance(instanceRequest);
			apiResponse.setSuccessful(HttpStatus.CREATED.is2xxSuccessful());
			apiResponse.setStatusCode(HttpStatus.CREATED.value());
			apiResponse.setResponseData(response);
			return apiResponse;
		} catch (InvalidRequestException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>();
			apiResponse.setResponseData(e.getMessage());
			apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
			apiResponse.setSuccessful(HttpStatus.BAD_GATEWAY.is4xxClientError());
			return apiResponse;
		}
	}
}
