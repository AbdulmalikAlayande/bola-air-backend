package com.example.airlinereservation.Controller;

import com.example.airlinereservation.dtos.request.AdminInvitationRequest;
import com.example.airlinereservation.dtos.request.CreateAdminRequest;
import com.example.airlinereservation.dtos.response.users.AdminInvitationResponse;
import com.example.airlinereservation.dtos.response.ApiResponse;
import com.example.airlinereservation.dtos.response.users.CreateAdminResponse;
import com.example.airlinereservation.exceptions.EmptyFieldException;
import com.example.airlinereservation.exceptions.FailedRegistrationException;
import com.example.airlinereservation.exceptions.FieldInvalidException;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.services.userservice.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("bola-air/admin/")
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class BolaAirAdminController {
	
	private AdminService adminService;
	
	@PostMapping("invite-admin/")
	public ApiResponse<?> inviteAdmin(@Valid @RequestBody AdminInvitationRequest invitationRequest){
		try {
			ApiResponse<AdminInvitationResponse> apiResponse = new ApiResponse<>();
			AdminInvitationResponse invitationResponse = adminService.inviteAdmin(invitationRequest);
			apiResponse.setResponseData(invitationResponse);
			apiResponse.setSuccessful(HttpStatus.CREATED.is2xxSuccessful());
			apiResponse.setStatusCode(HttpStatus.CREATED.value());
			return apiResponse;
		} catch (InvalidRequestException | EmptyFieldException | FieldInvalidException exception) {
			ApiResponse<String> apiResponse = new ApiResponse<>();
			apiResponse.setResponseData("An Error Occurred "+exception.getMessage());
			apiResponse.setSuccessful(false);
			apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return apiResponse;
		}
	}
	
	@PostMapping("create-admin-account/")
	public ApiResponse<?> createAdminAccount(@Valid @RequestBody CreateAdminRequest createAdminRequest){
		try {
			ApiResponse<CreateAdminResponse> apiResponse = new ApiResponse<>();
			CreateAdminResponse response = adminService.createAdminAccount(createAdminRequest);
			apiResponse.setResponseData(response);
			apiResponse.setSuccessful(HttpStatus.CREATED.is2xxSuccessful());
			apiResponse.setStatusCode(HttpStatus.CREATED.value());
			return apiResponse;
		} catch (FailedRegistrationException e) {
			ApiResponse<String> apiResponse = new ApiResponse<>();
			apiResponse.setResponseData(e.getMessage());
			apiResponse.setSuccessful(HttpStatus.BAD_REQUEST.is4xxClientError());
			apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return apiResponse;
		}
	}
}
