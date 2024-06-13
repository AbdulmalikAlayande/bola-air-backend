package com.example.airlinereservation.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CreateAdminRequest {
    @Valid
    @NotBlank(message = "This Field Cannot Be Empty Or Blank")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid Email: Email Must Match The Format Specified")
    private String email;
    @Valid
    @NotBlank(message = "This Field Cannot Be Empty Or Blank")
    @Size(max = 15, message = "This Field Should Not Contain More Than 15 Characters")
    private String firstName;
    @Valid
    @NotBlank(message = "This Field Cannot Be Empty Or Blank")
    @Size(max = 15, message = "This Field Should Not Contain More Than 15 Characters")
    private String lastName;
    @Valid
    @NotBlank(message = "This Field Cannot Be Empty Or Blank")
    private String phoneNumber;
    @Valid
    @NotBlank(message = "This Field Cannot Be Empty Or Blank")
    @Size(max = 15, min = 8, message = "Invalid Password Length: Password length must be between 8 and 15 characters")
    private String password;
    @Valid
    @NotBlank(message = "This Field Cannot Be Empty Or Blank")
    private String adminCode;

}
