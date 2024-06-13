package com.example.airlinereservation.Controller;

import com.example.airlinereservation.dtos.request.CustomerRequest;
import com.example.airlinereservation.dtos.request.LoginRequest;
import com.example.airlinereservation.dtos.response.ApiResponse;
import com.example.airlinereservation.dtos.response.users.CustomerResponse;
import com.example.airlinereservation.dtos.response.LoginResponse;
import com.example.airlinereservation.exceptions.FieldInvalidException;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.services.userservice.CustomerService;
import com.example.airlinereservation.exceptions.FailedRegistrationException;
import com.example.airlinereservation.exceptions.LoginFailedException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.airlinereservation.utils.Constants.ERROR_MESSAGE;

@RestController
@RequestMapping("bola-air/api/v3/")
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
@Validated
public class BolaAir_CustomerController {
	
	private CustomerService customerService;
	
	@PostMapping("register-customer/")
	@Validated
	public ApiResponse<?> registerCustomer(@RequestBody @Valid CustomerRequest customerRequest){
		CustomerResponse response = new CustomerResponse();
		try {
			response = customerService.registerNewCustomer(customerRequest);
			ApiResponse<CustomerResponse> apiResponse = new ApiResponse<>();
			apiResponse.setResponseData(response);
			apiResponse.setSuccessful(HttpStatus.CREATED.is2xxSuccessful());
			apiResponse.setStatusCode(HttpStatus.CREATED.value());
			System.out.println(apiResponse);
			return apiResponse;
		} catch (FailedRegistrationException | FieldInvalidException | InvalidRequestException exception) {
			log.info(ERROR_MESSAGE, exception.getMessage());
			log.info("Error:: ", exception);
			response.setMessage(exception.getMessage());
			ApiResponse<CustomerResponse> apiResponse= new ApiResponse<>();
			apiResponse.setResponseData(response);
			apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
			apiResponse.setSuccessful(HttpStatus.BAD_REQUEST.is4xxClientError());
			System.out.println(apiResponse);
			return apiResponse;
		}
	}
	
	@PostMapping("activate-account/{TOTP}")
	public ApiResponse<?> activateAccount(@PathVariable String TOTP){
		CustomerResponse customerResponse;
		try {
			ApiResponse<CustomerResponse> apiResponse = new ApiResponse<>();
			customerResponse = customerService.activateCustomerAccount(TOTP);
			apiResponse.setResponseData(customerResponse);
			apiResponse.setSuccessful(HttpStatus.CREATED.is2xxSuccessful());
			apiResponse.setStatusCode(HttpStatus.CREATED.value());
			System.out.println("api response at activate account ==> "+apiResponse);
			return apiResponse;
		} catch (InvalidRequestException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>();
			apiResponse.setSuccessful(false);
			apiResponse.setResponseData(e.getMessage());
			apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return apiResponse;
		}
	}
	
	@PostMapping("login-customer/")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
		try {
			LoginResponse loginResponse = customerService.login(loginRequest);
			ApiResponse<LoginResponse> response = new ApiResponse<>();
			response.setResponseData(loginResponse);
			response.setSuccessful(HttpStatus.OK.is2xxSuccessful());
			response.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (LoginFailedException e) {
			LoginResponse loginResponse = new LoginResponse();
			return null;
		}
	}
	
	@GetMapping("all-customers/")
	public ApiResponse<?> getAllCustomers(){
		try{
			List<CustomerResponse> foundCustomers = customerService.getAllCustomers();
			ApiResponse<List<CustomerResponse>> apiResponse = new ApiResponse<>();
			apiResponse.setResponseData(foundCustomers);
			apiResponse.setSuccessful(HttpStatus.FOUND.is2xxSuccessful());
			apiResponse.setStatusCode(HttpStatus.FOUND.value());
			System.out.println("api response at get all customers try ==> "+ apiResponse);
			return apiResponse;
		}catch (Throwable exception){
			ApiResponse<String> apiResponse = new ApiResponse<>();
			apiResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
			apiResponse.setSuccessful(HttpStatus.NOT_FOUND.is4xxClientError());
			apiResponse.setResponseData(exception.getMessage());
			System.out.println("api response at get all customers try ==> "+ apiResponse);
			return apiResponse;
		}
	}
}
