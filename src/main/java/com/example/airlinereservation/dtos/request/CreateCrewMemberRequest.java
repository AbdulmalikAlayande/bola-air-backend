package com.example.airlinereservation.dtos.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateCrewMemberRequest {
    private String password;
    private String userName;
    @NotBlank
    private String role;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String firstName;
    @NotEmpty
    private String lastName;
    private String fullName;
    private String country;
    private String state;
    private String postalCode;
    private String streetName;
    private String streetNumber;
    private String houseNumber;

}

