package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.data.model.enums.Role;
import com.example.airlinereservation.data.model.flight.FlightInstance;
import com.example.airlinereservation.data.model.persons.*;
import com.example.airlinereservation.data.repositories.AddressRepository;
import com.example.airlinereservation.data.repositories.CrewMemberRepository;
import com.example.airlinereservation.data.repositories.UserBioDataRepository;
import com.example.airlinereservation.dtos.request.*;
import com.example.airlinereservation.dtos.response.flight.FlightScheduleResponse;
import com.example.airlinereservation.dtos.response.users.CreateCrewMemberResponse;
import com.example.airlinereservation.dtos.response.users.CrewMemberResponse;
import com.example.airlinereservation.exceptions.EmptyFieldException;
import com.example.airlinereservation.exceptions.FieldInvalidException;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import com.example.airlinereservation.services.notifications.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.example.airlinereservation.utils.Constants.*;

@Service
@AllArgsConstructor
@Slf4j
public class BolaAirCrewMemberService implements CrewMemberService {

    private CrewMemberRepository crewMemberRepository;
    private ModelMapper mapper;
    private CrewMemberManagementService managementService;
    private UserBioDataRepository userBioDataRepository;
    private Validator validator;
    private AddressRepository addressRepository;
    
    
    @Override
    public CreateCrewMemberResponse createCrewMember(CreateCrewMemberRequest createCrewMemberRequest) throws EmptyFieldException, FieldInvalidException {
        checkForNullFields(createCrewMemberRequest);
        rejectCrewMemberIfCrewMemberAlreadyExists(createCrewMemberRequest);
        validator.validatePassword(createCrewMemberRequest.getPassword());
        CrewMember newCrewMember = new CrewMember();
        Address mappedAddress = mapper.map(createCrewMemberRequest, Address.class);
        UserBioData mappedBio = mapper.map(createCrewMemberRequest, UserBioData.class);
        Address savedAddress = addressRepository.save(mappedAddress);
        mappedBio.setAddress(savedAddress);
        UserBioData savedBio = userBioDataRepository.save(mappedBio);
        newCrewMember.setBioData(savedBio);
        newCrewMember.setAvailable(true);
        newCrewMember.setRole(Role.CREW_MEMBER);
        CrewMember savedCrewMember = crewMemberRepository.save(newCrewMember);
        managementService.addCrewMemberToDepartment(savedCrewMember);
        CreateCrewMemberResponse createCrewMemberResponse = new CreateCrewMemberResponse();
        createCrewMemberResponse.setMessage("Registration Successful");
        return createCrewMemberResponse;
    }
    private void checkForNullFields(CreateCrewMemberRequest createCrewMemberRequest) throws EmptyFieldException {
        try {
            Field[] declaredFields = createCrewMemberRequest.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Object accessedField = field.get(createCrewMemberRequest);
                if (accessedField == null || (accessedField instanceof String && accessedField.toString().isEmpty())) {
                    String errorMessage = String.format(EMPTY_FIELD_MESSAGE, field.getName());
                    throw new EmptyFieldException(String.format(INCOMPLETE_DETAILS_MESSAGE, errorMessage));
                }
            }
        } catch (Exception e){
            throw new EmptyFieldException(e.getMessage());
        }
    }
    
    private void rejectCrewMemberIfCrewMemberAlreadyExists(CreateCrewMemberRequest createCrewMemberRequest) {
        Optional<UserBioData> foundBio = userBioDataRepository.findByEmailAndPassword(createCrewMemberRequest.getEmail(), createCrewMemberRequest.getPassword());
//        boolean crewMemberExists = crewMemberRepository.existsByUserName(createCrewMemberRequest.getUserName());
    }

    @Override
    public void deleteCrewMemberById(String id) throws InvalidRequestException {
        Optional<CrewMember> foundCrewMember = crewMemberRepository.findById(id);
        if (foundCrewMember.isEmpty())
            throw new InvalidRequestException(String.format(INVALID_REQUEST_MESSAGE, "Crew member", "id", id));
        crewMemberRepository.deleteById(id);

    }

    @Override
    public FlightInstance assignCrewMember(FlightInstance flightInstance) {
        return null;
    }

    @Override
    public void deleteCrewMemberByUsername(String userName) throws InvalidRequestException {
//        boolean memberExists = crewMemberRepository.existsByUserName(userName);
//        if (!memberExists)
//            throw new InvalidRequestException(String.format(INVALID_REQUEST_MESSAGE, "Crew member", "username", userName));
//        crewMemberRepository.deleteByUserName(userName);
    }
    
    @Override
    public long getCountOfCrewMembers() {
        return crewMemberRepository.count();
    }
    
    @Override
    public boolean existsByUsername(String userName) {
        return false;
//        return crewMemberRepository.existsByUserName(userName);
    }

    @Override
    public Optional<CrewMember> findCrewMemberByUserName(String userName) throws InvalidRequestException {
//        boolean crewMember = crewMemberRepository.existsByUserName(userName);
//        if(!crewMember)
//            throw new InvalidRequestException(String.format(INVALID_REQUEST_MESSAGE,"Crew member", "username", userName));
//        return crewMemberRepository.findByUserName(userName);
        return Optional.empty();
    }

    @Override
    public CrewMemberResponse updateDetailsOfRegisteredCrewMember(@NotNull UpdateRequest updateRequest) {
        CrewMemberResponse crewMemberResponse = new CrewMemberResponse();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
//        Optional<CrewMember> crewMemberBio = crewMemberRepository.findByUserName(updateRequest.getNewUserName());
       return null;
    }
    
    @Override
    public FlightScheduleResponse viewFlightSchedule(ViewFlightScheduleRequest flightScheduleRequest) {
        return null;
    }
}
