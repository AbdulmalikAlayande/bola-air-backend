package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.dtos.Request.LoginRequest;
import com.example.airlinereservation.dtos.Request.CustomerRequest;
import com.example.airlinereservation.dtos.Request.UpdateRequest;
import com.example.airlinereservation.dtos.Response.LoginResponse;
import com.example.airlinereservation.dtos.Response.CustomerResponse;
import com.example.airlinereservation.exceptions.FailedRegistrationException;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.exceptions.LoginFailedException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static com.example.airlinereservation.utils.Constants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest()
class CustomerServiceTest {
	@Autowired
	CustomerService customerService;
	CustomerResponse customerResponse;
	UpdateRequest updateRequest;
	
	@BeforeEach
	@SneakyThrows
	public void startAllTestWith() {
		customerService.removeAll();
		updateRequest = new UpdateRequest();
		customerResponse = customerService.registerNewCustomer(
										CustomerRequest.builder()
		                               .phoneNumber("567890234")
		                               .firstName("Alayande")
		                               .lastName("Abdulmalik")
		                               .email("alaabdulmalik03@gmail.com")
		                               .password("Ayanniyi@20")
		                               .build()
		);
	}
	
	@AfterEach
	public void endEachTestWith() {
		customerService.removeAll();
	}
	
	@SneakyThrows
	@Test void testThatPassengerTriesToRegisterWithIncompleteDetails_ExceptionIsThrown(){
		assertThatThrownBy(()-> customerService.registerNewCustomer(buildIncompletePassenger()))
											.as("")
											.isInstanceOf(NullPointerException.class);
	}
	
	@Test void testThatPassengerTriesToRegisterUsingDetailsWithIncorrectFormat_RegistrationFailedExceptionIsThrown() {
		assertThatThrownBy(() -> customerService.registerNewCustomer(buildPassengerWithIncorrectFormatDetails()),
				"Invalid Email Format")
				.as("Please enter a valid email format", "")
				.isInstanceOf(FailedRegistrationException.class)
				.hasMessageContaining("Please enter a valid email format");
	}
	
	@SneakyThrows
	@Test void testThatPassengerCanRegisterSuccessfully_IfAllChecksArePassed(){
		customerResponse = customerService.registerNewCustomer(buildPassenger1());
		assertThat(customerService.getCountOfCustomers()).isNotZero();
		assertThat(customerService.getCountOfCustomers()).isGreaterThan(BigInteger.ZERO.intValue());
		assertThat(customerResponse).isNotNull();
	}
	
	@Test void testThatOtpIsGenerated_AndSentToTheUserToActivateTheirAccount(){
	}
	
	@Test
	@SneakyThrows
	void testThatAccountActivationIsSuccessful_IfTheOtpEnteredIdCorrect(){
		CustomerResponse response = customerService.activateCustomerAccount(String.valueOf(customerResponse.getOtp()));
		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo(SUCCESSFUL_ACTIVATION_MESSAGE);
	}
	
	@SneakyThrows
	@Test void testThatPassengerCanUpdateTheirDetails(){
		updateRequest.setFirstName("Alibaba");
		updateRequest.setEmail("alibaba@gmail.com");
		updateRequest.setPhoneNumber("08056472356");
		updateRequest.setUserName("mirah");
		updateRequest.setNewUserName("mithra");
		CustomerResponse updateResponse = customerService.updateDetailsOfRegisteredCustomer(updateRequest);
		assertThat(updateResponse).isNotNull();
		assertThat(updateResponse.getEmail()).isEqualTo(updateRequest.getEmail());
		Optional<CustomerResponse> foundPassenger = customerService.findCustomerByUserName(updateRequest.getNewUserName());
		assertThat(foundPassenger.isPresent()).isTrue();
		foundPassenger.ifPresent(passenger-> assertThat(passenger.getEmail()).isEqualTo(updateRequest.getEmail()));
	}
	private CustomerRequest buildIncompletePassenger() {
		return CustomerRequest.builder().email("theeniolasamuel@gmail.com").firstName("Samuel")
				       .lastName("Eniola").password("coco@22").build();
	}
	
	private CustomerRequest buildPassengerWithIncorrectFormatDetails() {
		return CustomerRequest.builder().password("Obim").email("emailgmail")
				       .lastName("Obinali").firstName("Goodness").phoneNumber("08045673421").build();
	}
	private CustomerRequest buildPassenger1() {
		return CustomerRequest.builder()
				       .password("zainab@64")
				       .lastName("Alayande")
				       .firstName("Zainab")
				       .phoneNumber("08030669508")
				       .email("alayandezainab64@gmail.com")
				       .build();
	}
	private CustomerRequest buildPassenger() {
		return CustomerRequest.builder()
				       .password("ayanniyi@20")
				       .lastName("Alayande")
				       .firstName("Abdulmalik")
				       .phoneNumber("07036174617")
				       .email("alaabdulmalik03@gmail.com")
				       .build();
	}
	
	@SneakyThrows
	@Test void findSavedPassengerWithAUsernameThatDoesNotExist_InvalidRequestExceptionIsThrown(){
		assertThrowsExactly(InvalidRequestException.class, ()-> customerService.findCustomerByUserName("mithra"),
				"Request Failed:: Invalid Username");
	}
	@SneakyThrows
	@Test void findSavedPassengerWithUsername_PassengerWithTheSaidUsernameIsFound(){
		Optional<CustomerResponse> response = customerService.findCustomerByUserName("mirah");
		assertThat(response.isPresent()).isTrue();
		response.ifPresent(passengerResponse -> {
			assertThat(passengerResponse).isNotNull();
			assertThat(passengerResponse).isInstanceOf(CustomerResponse.class);
		});
	}
	
	@Test void testThatWhenTokenHasExpiredAnotherOneIsGenerated(){
	
	}
	
	@Test void testThatUserTriesToLoginWithoutSigningUpLoginFailedExceptionIsThrown(){
		LoginRequest request = LoginRequest.builder().email("alamala@gmail.com").password("alamala@42").username("alamala1").build();
		assertThrows(LoginFailedException.class, ()-> customerService.login(request), "Login Failed:: You do not have an account with us, Please register to create one");
	}
	
	@Test void testThatUserTriesToLoginWithoutValidOrIncompleteCredentialsLoginFailedExceptionIsThrown(){
		LoginRequest request = LoginRequest.builder().username("mirah").email("ololadeayandunni@gmail.com").build();
		assertThrows(LoginFailedException.class, ()-> customerService.login(request), "Login Failed:: Please provide the full details requested in the correct format");
	}
	
	@SneakyThrows
	@DisplayName("Login is successful when all credentials are valid")
	@Test void loginTest(){
		LoginRequest request = LoginRequest.builder()
				                       .username("mirah").email("ololadeayandunni@gmail.com")
				                       .password("ayandunni#$2008")
				                       .build();
		LoginResponse response = customerService.login(request);
		assertThat(response.getMessage()).isNotEmpty();
		assertThat(response.getUsername()).isEqualTo(request.getUsername());
	}
	
	@Test void testThatUserTriesToLogInWhenLoginSessionIsStillOn(){
	
	}
	
	@Test
	void findSavedPassengerWithIdThatDoesExist_InvalidRequestExceptionIsThrown(){
		assertThrowsExactly(RuntimeException.class, ()-> customerService.findCustomerById("892ffr0ilj84aas787t274gf7qwerty8"),
				"Request Failed:: Invalid Id");
	}
	
	//todo to fail
	@SneakyThrows
	@Test
	@Disabled
	public void findSavedPassengerWithId_PassengerWithTheSaidIdIsFound(){
		Optional<CustomerResponse> response = customerService.findCustomerById("");
		response.ifPresent(passengerResponse -> {
			assertThat(passengerResponse).isNotNull();
			assertThat(passengerResponse).isInstanceOf(CustomerResponse.class);
		});
	}
	@SneakyThrows
	@Test void removePassengerByUserNameTest(){
		customerService.registerNewCustomer(buildPassenger());
	}
	
	@SneakyThrows
	@Test void getAllPassengersTest(){
		List<CustomerResponse> allPassengersPresent = customerService.getAllCustomers();
		allPassengersPresent.forEach(passengerResponse-> assertThat(passengerResponse).isNotNull());
		assertThat(allPassengersPresent.size()).isEqualTo(customerService.getCountOfCustomers());
	}
	
	@SneakyThrows
	@Disabled
	@Test void removePassengerByIdTest(){
		assertEquals(BigInteger.TWO.intValue(), customerService.getCountOfCustomers());
	}
	
}
