package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.data.model.Passenger;
import com.example.airlinereservation.data.model.persons.*;
import com.example.airlinereservation.data.repositories.*;
import com.example.airlinereservation.dtos.request.*;
import com.example.airlinereservation.dtos.response.*;
import com.example.airlinereservation.dtos.response.flight.FlightResponse;
import com.example.airlinereservation.dtos.response.users.CustomerResponse;
import com.example.airlinereservation.exceptions.*;
import com.example.airlinereservation.services.notifications.Validator;
import com.example.airlinereservation.services.notifications.mail.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

import static com.example.airlinereservation.data.model.enums.Role.USER;
import static com.example.airlinereservation.exceptions.Exceptions.*;
import static com.example.airlinereservation.utils.Constants.*;

@Service
@AllArgsConstructor
@Slf4j
public class BolaAirCustomerService implements CustomerService {
	
	private Validator validator;
	private CustomerRepository customerRepository;
	private UserBioDataRepository userBioDataRepository;
	private ModelMapper mapper;
	private OTPService otpService;
	private MailService mailer;
	
	@Override
	@Transactional(rollbackFor = {SQLException.class, FailedRegistrationException.class})
	public CustomerResponse registerNewCustomer(@NotNull CustomerRequest customerRequest) throws FailedRegistrationException {
		CustomerResponse customerResponse = new CustomerResponse();
		try {
			validator.validateEmail(customerRequest.getEmail());
			validator.validatePassword(customerRequest.getPassword());
			UserBioData biodata = mapper.map(customerRequest, UserBioData.class);
			biodata.setOTPs(new ArrayList<>());
			UserBioData savedBio = userBioDataRepository.save(biodata);
			Customer customer = new Customer();
			customer.setRole(USER);
			customer.setBioData(savedBio);
			customerRepository.save(customer);
			
			OTP createdOTP = createOtp(biodata.getEmail());
			long otpData = createdOTP.getData();
			mailer.sendOtp(buildNotificationRequest(biodata.getFirstName(), biodata.getEmail(), otpData));
			
			savedBio.getOTPs().add(createdOTP);
			userBioDataRepository.save(savedBio);
			mapper.map(savedBio, customerResponse);
			customerResponse.setMessage(REGISTRATION_SUCCESSFUL_MESSAGE);
			customerResponse.setOtp(otpData);
		} catch (FieldInvalidException | InvalidRequestException exception) {
			throwFailedRegistrationException(exception);
		}
		return customerResponse;
	}
	
	@Override
	@Transactional(rollbackFor = {SQLException.class, FailedRegistrationException.class})
	public CustomerResponse activateCustomerAccount(String OTP) throws InvalidRequestException {
		OTP otp = otpService.verifiedOtp(OTP);
		return userBioDataRepository.findByEmail(otp.getUserEmail())
								    .map(userBioData -> buildCustomerResponse(userBioData, SUCCESSFUL_ACTIVATION_MESSAGE))
								    .orElseThrow(()->new InvalidRequestException("USER WITH EMAIL NOT FOUND"));
	}
	
	private CustomerResponse buildCustomerResponse(UserBioData bioData, String message) {
		return CustomerResponse.builder()
						       .message(message)
						       .lastName(bioData.getLastName())
						       .phoneNumber(bioData.getPhoneNumber())
						       .email(bioData.getEmail())
						       .build();
	}
	
	public OTP createOtp(String email){
		OTP generatedTotp = otpService.generateTOTP(email);
		return otpService.saveOTP(generatedTotp);
	}
	
	private NotificationRequest buildNotificationRequest(String firstName, String email, long otp){
		return NotificationRequest.builder()
				       .email(email)
				       .code(String.valueOf(otp))
				       .OTP(otp)
				       .mailPath("otp")
				       .firstName(firstName)
				       .build();
	}
	@Override
	public List<FlightResponse> viewAvailableFLights() {
		return null;
		
	}
	
	@Override
	public CustomerResponse updateDetailsOfRegisteredCustomer(@NotNull UpdateRequest updateRequest) {
		CustomerResponse response = new CustomerResponse();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		return response;
	}
	
	@Override
	public LoginResponse login(LoginRequest loginRequest) {
	    return new LoginResponse();
	}

	@Override public Optional<CustomerResponse> findCustomerById(String passengerId) {
		CustomerResponse response = new CustomerResponse();
		Optional<Customer> foundCustomer = customerRepository.findById(passengerId);
		return Optional.of(foundCustomer
				       .map(passenger -> {
						   mapper.map(passenger, response);
						   return response;
					   })
				       .orElseThrow(()-> {
					       try {
						       return throwInvalidRequestException(String.format(INVALID_REQUEST_MESSAGE, "User", "id", passengerId));
					       } catch (InvalidRequestException e) {
						       throw new RuntimeException(e);
					       }
				       }));
	}
	
	@Override
	public List<CustomerResponse> getAllCustomers() {
		List<Customer> allCustomers = customerRepository.findAll();
		return new ArrayList<>(allCustomers.stream().map(passenger -> {
			CustomerResponse response = new CustomerResponse();
			mapper.map(passenger.getBioData(), response);
			return response;
		}).toList());
	}
	
	@Override
	public Optional<CustomerResponse> findCustomerByEmailAndPassword(String email, String password) {
		Optional<UserBioData> foundBio = userBioDataRepository.findByEmailAndPassword(email, password);
		CustomerResponse response = new CustomerResponse();
		foundBio.ifPresent(bioData -> {
			Optional<Customer> foundCustomer = customerRepository.findByBioData(bioData);
			foundCustomer.ifPresent(passenger -> mapper.map(bioData, response));
		});
		return Optional.of(response);
	}
	
	@Override public Optional<CustomerResponse> findCustomerByUserName(String userName) {
		return Optional.of(new CustomerResponse());
	}
	
	@Override public long getCountOfCustomers() {
		return customerRepository.count();
	}
	
	@Override
	public Optional<Passenger> findPassengerByUserNameForAdmin(String passengerUsername) {
		return Optional.empty();
	}
	
	@Override
	public void removeAll() {
		customerRepository.deleteAll();
		userBioDataRepository.deleteAll();
	}
}
