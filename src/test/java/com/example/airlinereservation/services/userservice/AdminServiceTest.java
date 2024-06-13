
package com.example.airlinereservation.services.userservice;


import com.example.airlinereservation.dtos.request.AdminInvitationRequest;
import com.example.airlinereservation.dtos.request.CreateAdminRequest;
import com.example.airlinereservation.dtos.response.users.AdminInvitationResponse;
import com.example.airlinereservation.dtos.response.users.CreateAdminResponse;
import com.example.airlinereservation.dtos.response.users.GetUserResponse;
import com.example.airlinereservation.exceptions.FieldInvalidException;
import com.example.airlinereservation.exceptions.UserNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.airlinereservation.utils.Constants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class AdminServiceTest {

	@Autowired
	private AdminService adminService;
	CreateAdminRequest createAdminRequest;
	CreateAdminResponse createAdminResponse;
	AdminInvitationResponse response;
	
	@BeforeEach
	@SneakyThrows
	public void setUp() {
		adminService.deleteAll();
		createAdminRequest = new CreateAdminRequest();
		createAdminResponse = new CreateAdminResponse();
		AdminInvitationRequest invitationRequest = new AdminInvitationRequest();
		invitationRequest.setEmail("alaabdulmalik03@gmail.com");
		response = adminService.inviteAdmin(invitationRequest);
	}
	
	@AfterEach
	void tearDown() {
	}
	
	@SneakyThrows
	@Test void testThatAdminCanBeInvitedToOurApplication(){
		assertThat(response.getMessage()).isEqualTo("An Invitation Mail Has Been Sent To alaabdulmalik03@gmail.com");
	}
	
	@Test void testThatBeforeInvitation_AdminEmailMustBeCorrect(){
		AdminInvitationRequest invitationRequest = new AdminInvitationRequest();
		invitationRequest.setEmail("alaabdulmalik03@m");
		assertThatThrownBy(()-> response = adminService.inviteAdmin(invitationRequest)).isInstanceOf(FieldInvalidException.class)
																					   .hasMessageContaining(INVALID_EMAIL_FORMAT);
	}
	@Test void testThatAdminEmailIsSaved_AfterInvitation(){
		GetUserResponse foundAdminRef = adminService.findByEmail("alaabdulmalik03@gmail.com");
		assertThat(foundAdminRef).isNotNull();
		assertThat(foundAdminRef.getEmail()).isEqualTo("alaabdulmalik03@gmail.com");
	}
	@SneakyThrows
	@Test
	public void createAdminAccountTest(){
		CreateAdminResponse adminResponse = adminService.createAdminAccount(buildAdmin());
		assertThat(adminResponse.getMessage()).isEqualTo("Admin Account created successfully");
	}
	
	@SneakyThrows
	@Test void createAdminAccount_WithoutInvitation_ExceptionIsThrown(){
		assertThatThrownBy(()->{
			CreateAdminResponse adminResponse = adminService.createAdminAccount(buildUninvitedAdmin());
		})
		.isInstanceOf(UserNotFoundException.class)
		.hasMessageContaining("Admin With Email Ahoy@gmail.com Does Not Exist");
	}
	
	private CreateAdminRequest buildUninvitedAdmin() {
		return CreateAdminRequest.builder()
				       .firstName("Hello")
				       .lastName("Ahoy")
				       .email("Ahoy@gmail.com")
				       .adminCode("23456777")
				       .phoneNumber("07036174617")
				       .password("P@ssword")
				       .build();
	}
	
	@Test void testThatAdminTriesToCreateAccountWithInvalidData_ExceptionIsThrown(){
	
	}
	@Test void testThatAdminTriesToCreateAccountTwiceInvalidRequestExceptionIsThrown(){
	
	}
	@Test void testThatAdminCanAddAirCraftToTheListOfAirCraft(){
	
	}
	public CreateAdminRequest buildAdmin(){
        return CreateAdminRequest.builder()
					             .lastName("Alhaji")
		                         .adminCode(response.getCode())
		                         .firstName("Abdulmalik")
		                         .email("alaabdulmalik03@gmail.com")
					             .phoneNumber("08081493711")
		                         .password("P@assword")
		                         .build();
	}
	
	@Test void testThatACrewHasToExistBeforeTheyCanBeAssignedToAFlight(){

	}

	@Test void testThatAdminCanAddCrewMember(){
		//CrewMember crewMember = new CrewMember();
		//adminService.addCrewMember();

	
	}

	
	@Test void testThatAdminCanAssignCrewMember(){
	
	}

	
	@Test void testThatAdminCanAssignCrewMember_AssignedCrewMembersAreNoLongerAvailable(){
	
	}
	
	@Test void testThatAdminTriesToReAssignAnUnavailableCrewMember_InvalidRequestExceptionIsThrown(){
	
	}

}
