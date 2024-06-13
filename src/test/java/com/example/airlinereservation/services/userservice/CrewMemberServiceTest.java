package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.data.model.persons.CrewMember;
import com.example.airlinereservation.dtos.request.CreateCrewMemberRequest;
import com.example.airlinereservation.dtos.request.UpdateRequest;
import com.example.airlinereservation.dtos.response.users.CreateCrewMemberResponse;
import com.example.airlinereservation.dtos.response.users.CrewMemberResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CrewMemberServiceTest {
    @Autowired
    private CrewMemberService crewMemberService;
    private CreateCrewMemberRequest createCrewMemberRequest;
    private UpdateRequest updateRequest;

    @BeforeEach
    @SneakyThrows
    public void startAllTestWith() {
        createCrewMemberRequest = buildCrewMember();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test void testThatACrewHasToExistBeforeTheyCanBeAssignedToAFlight(){
        CreateCrewMemberResponse createCrewMemberResponse = crewMemberService.createCrewMember(buildCrewMember());
        assertThat(createCrewMemberResponse.getMessage()).isEqualTo("Crew member created successfully");
    }

    public static CreateCrewMemberRequest buildCrewMember(){
        return CreateCrewMemberRequest.builder()
                                .userName("Dan123")
                                .phoneNumber("08012343455")
                                .password("pass123")
                                .email("dan@gmail.com")
                                .fullName("John Doe")
                                .state("Lagos")
                                .houseNumber("22")
                                .streetName("Songo Street")
                                .streetNumber("2")
                                .postalCode("10006")
                                .country("Nigeria")
                                .firstName("John")
                                .lastName("Doe")
                                .email("johndoe@gmail.com")
                               .build();
    }

    @SneakyThrows
    @Test void testThatAnExistingCrewMemberCanBeDeletedByUsername() {
        crewMemberService.createCrewMember(createCrewMemberRequest);
        long numberOfCrewMembersBefore = crewMemberService.getCountOfCrewMembers();
        crewMemberService.deleteCrewMemberByUsername("Dan123");
        assertThat(crewMemberService.existsByUsername(createCrewMemberRequest.getUserName())).isFalse();
        long numberOfCrewMembersAfter = crewMemberService.getCountOfCrewMembers();
        assertThat(numberOfCrewMembersBefore).isGreaterThan(numberOfCrewMembersAfter);
    }

    @SneakyThrows
    @Test void testThatExistingCrewMemberCanBeFoundByUserName() {
        crewMemberService.createCrewMember(createCrewMemberRequest);
        Optional<CrewMember> foundCrewMember = crewMemberService.findCrewMemberByUserName(createCrewMemberRequest.getUserName());
        assertThat(foundCrewMember).isPresent();
    }

    @SneakyThrows
    @Test void testThatAnExistingCrewMemberCanUpdateRegistrationDetails(){
        updateRequest.setEmail("me@gmail.com");
        updateRequest.setFirstName("chris");
        updateRequest.setUserName("malik33");
        updateRequest.setNewUserName("rich123");
        CrewMemberResponse response = crewMemberService.updateDetailsOfRegisteredCrewMember(updateRequest);
        assertThat(response).isNotNull();
        Optional<CrewMember> foundCrewMember = crewMemberService.findCrewMemberByUserName(createCrewMemberRequest.getUserName());
        assertThat(foundCrewMember).isPresent().isPresent();
        foundCrewMember.ifPresent(crewMember -> {
            assertThat(crewMember.getBioData().getEmail()).isEqualTo(updateRequest.getEmail());
        });




    }

}