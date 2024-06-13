package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.data.model.Passenger;
import com.example.airlinereservation.dtos.request.LoginRequest;
import com.example.airlinereservation.dtos.request.CustomerRequest;
import com.example.airlinereservation.dtos.request.UpdateRequest;
import com.example.airlinereservation.dtos.response.flight.FlightResponse;
import com.example.airlinereservation.dtos.response.LoginResponse;
import com.example.airlinereservation.dtos.response.users.CustomerResponse;
import com.example.airlinereservation.exceptions.FailedRegistrationException;
import com.example.airlinereservation.exceptions.FieldInvalidException;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.exceptions.LoginFailedException;

import java.util.List;
import java.util.Optional;
public interface CustomerService {
	
	
	CustomerResponse registerNewCustomer(CustomerRequest passengerRequest) throws FailedRegistrationException, FieldInvalidException, InvalidRequestException;
	
	CustomerResponse activateCustomerAccount(String OTP) throws InvalidRequestException;
	
	List<FlightResponse> viewAvailableFLights();
	CustomerResponse updateDetailsOfRegisteredCustomer(UpdateRequest updateRequest);
	LoginResponse login(LoginRequest loginRequest) throws LoginFailedException;
	Optional<CustomerResponse> findCustomerById(String passengerId) throws InvalidRequestException;
	long getCountOfCustomers();
	List<CustomerResponse> getAllCustomers();
	Optional<CustomerResponse> findCustomerByEmailAndPassword(String email, String password) throws InvalidRequestException;
	
	Optional<CustomerResponse> findCustomerByUserName(String userName) throws InvalidRequestException;
	
	Optional<Passenger> findPassengerByUserNameForAdmin(String passengerUsername);
	
	void removeAll();
}
