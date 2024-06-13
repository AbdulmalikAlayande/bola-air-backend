package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.dtos.request.AdminInvitationRequest;
import com.example.airlinereservation.dtos.request.CreateAdminRequest;
import com.example.airlinereservation.dtos.request.CreateCrewMemberRequest;
import com.example.airlinereservation.dtos.request.FlightRequest;
import com.example.airlinereservation.dtos.response.flight.FlightResponse;
import com.example.airlinereservation.dtos.response.users.AdminInvitationResponse;
import com.example.airlinereservation.dtos.response.users.CreateAdminResponse;
import com.example.airlinereservation.dtos.response.users.CreateCrewMemberResponse;
import com.example.airlinereservation.dtos.response.users.GetUserResponse;
import com.example.airlinereservation.exceptions.EmptyFieldException;
import com.example.airlinereservation.exceptions.FailedRegistrationException;
import com.example.airlinereservation.exceptions.FieldInvalidException;
import com.example.airlinereservation.exceptions.InvalidRequestException;

public interface AdminService {

    CreateAdminResponse createAdminAccount(CreateAdminRequest createAdminRequest) throws FailedRegistrationException;
	AdminInvitationResponse inviteAdmin(AdminInvitationRequest invitationRequest) throws InvalidRequestException, FieldInvalidException, EmptyFieldException;
    CreateCrewMemberResponse addCrewMember(CreateCrewMemberRequest createCrewMemberRequest) throws EmptyFieldException, IllegalAccessException, FieldInvalidException;
    GetUserResponse findByEmail(String email);
	FlightResponse addNewFlight(FlightRequest flightRequest) throws InvalidRequestException;
	
	void deleteAll();
}
 