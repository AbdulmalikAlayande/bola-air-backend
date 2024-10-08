package app.bola.flywell.services.userservice;


import app.bola.flywell.data.model.Passenger;
import app.bola.flywell.dtos.Request.CustomerRequest;
import app.bola.flywell.dtos.Request.LoginRequest;
import app.bola.flywell.dtos.Request.UpdateRequest;
import app.bola.flywell.dtos.Response.CustomerResponse;
import app.bola.flywell.dtos.Response.FlightResponse;
import app.bola.flywell.dtos.Response.LoginResponse;
import app.bola.flywell.exceptions.FailedRegistrationException;
import app.bola.flywell.exceptions.FieldInvalidException;
import app.bola.flywell.exceptions.InvalidRequestException;
import app.bola.flywell.exceptions.LoginFailedException;

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
