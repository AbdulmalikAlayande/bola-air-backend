package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.data.model.enums.Role;
import com.example.airlinereservation.data.model.persons.Admin;
import com.example.airlinereservation.data.model.persons.UserBioData;
import com.example.airlinereservation.data.repositories.AdminRepository;
import com.example.airlinereservation.data.repositories.UserBioDataRepository;
import com.example.airlinereservation.dtos.request.*;
import com.example.airlinereservation.dtos.response.*;
import com.example.airlinereservation.dtos.response.flight.FlightResponse;
import com.example.airlinereservation.dtos.response.users.AdminInvitationResponse;
import com.example.airlinereservation.dtos.response.users.CreateAdminResponse;
import com.example.airlinereservation.dtos.response.users.CreateCrewMemberResponse;
import com.example.airlinereservation.dtos.response.users.GetUserResponse;
import com.example.airlinereservation.exceptions.EmptyFieldException;
import com.example.airlinereservation.exceptions.FieldInvalidException;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.exceptions.UserNotFoundException;
import com.example.airlinereservation.services.flightservice.FlightService;
import com.example.airlinereservation.services.notifications.Validator;
import com.example.airlinereservation.services.notifications.mail.MailService;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class BolaAirAdminService implements AdminService{
    
    private AdminRepository adminRepository;
    private UserBioDataRepository bioDataRepository;
    private CrewMemberService crewMemberService;
    private Validator validator;
    private MailService mailService;
    private ModelMapper mapper;
    private FlightService flightService;

    @Override
    @Transactional(rollbackFor = {SQLException.class, DataIntegrityViolationException.class, ConstraintViolationException.class})
    public CreateAdminResponse createAdminAccount(CreateAdminRequest createAdminRequest) {
        AtomicReference<CreateAdminResponse> responseRef = new AtomicReference<>();
        Optional<Admin> foundAdminRef = adminRepository.findByEmail(createAdminRequest.getEmail());
        foundAdminRef.ifPresentOrElse((foundAdmin)->{
            if (Objects.equals(foundAdmin.getAdminCode(), createAdminRequest.getAdminCode())) {
                UserBioData mappedBio = mapper.map(createAdminRequest, UserBioData.class);
                UserBioData saveBioData = bioDataRepository.save(mappedBio);
                foundAdmin.setBioData(saveBioData);
                foundAdmin.setRole(Role.ADMIN);
                adminRepository.save(foundAdmin);
                responseRef.set(mapper.map(saveBioData, CreateAdminResponse.class));
            }
            else throw new UserNotFoundException("Invalid Code, Please Enter The Code That Was Sent To You at %s".formatted(createAdminRequest.getEmail()));
        }, ()-> {throw new UserNotFoundException("Admin With Email %s Does Not Exist".formatted(createAdminRequest.getEmail()));});
        responseRef.get().setMessage("Admin Account created successfully");
	    return responseRef.get();
    }


    @Override
    public AdminInvitationResponse inviteAdmin(AdminInvitationRequest invitationRequest) throws InvalidRequestException, FieldInvalidException, EmptyFieldException {
        if (invitationRequest.getEmail() == null || invitationRequest.getEmail().isEmpty() || invitationRequest.getEmail().isBlank())
            throw new EmptyFieldException("Email Cannot Be Blank Or Empty Spaces");
        else {
            validator.validateEmail(invitationRequest.getEmail());
            String adminCode = generateAdminCode(invitationRequest.getEmail());
            Admin admin = new Admin();
            admin.setEmail(invitationRequest.getEmail());
            admin.setAdminCode(adminCode);
            adminRepository.save(admin);
            NotificationRequest notificationRequest = buildNotificationRequest(invitationRequest, adminCode);
            ResponseEntity<NotificationResponse> response = mailService.sendAdminInvitationEmail(notificationRequest);
            System.out.println(response);
            return AdminInvitationResponse.builder()
                           .email(invitationRequest.getEmail())
                           .code(adminCode)
                           .message("An Invitation Mail Has Been Sent To " + invitationRequest.getEmail())
                           .build();
        }
    }
    
    private NotificationRequest buildNotificationRequest(AdminInvitationRequest invitationRequest, String code) {
        return NotificationRequest.builder()
                                  .email(invitationRequest.getEmail())
                                  .code(code)
                                  .mailPath("invite")
                                  .firstName("Abdulmalik")
                                  .build();
    }
    
    private String generateAdminCode(String adminEmail) {
        String adminCodePrefix = "__BOLA--AIR__";
        String prefixHashCode = String.valueOf(adminCodePrefix.hashCode());
        String adminEmailHashCode = String.valueOf(adminEmail.hashCode());
        return prefixHashCode+adminEmailHashCode;
    }
    
    @Override
    public CreateCrewMemberResponse addCrewMember(CreateCrewMemberRequest createCrewMemberRequest) throws EmptyFieldException, IllegalAccessException, FieldInvalidException {
        return crewMemberService.createCrewMember(createCrewMemberRequest);
    }


    @Override
    public GetUserResponse findByEmail(String email) {
        Optional<Admin> foundAdminRef = adminRepository.findByEmail(email);
        return foundAdminRef.map(bioData -> mapper.map(bioData, GetUserResponse.class))
                            .orElseThrow(()-> new UserNotFoundException("User With Email %s Does Not Exist".formatted(email)));
    }
    
    @Override
    public FlightResponse addNewFlight(FlightRequest flightRequest) throws InvalidRequestException {
        return flightService.addFlight(flightRequest);
    }
    
    @Override
    public void deleteAll() {
        adminRepository.deleteAll();
    }
    
}
