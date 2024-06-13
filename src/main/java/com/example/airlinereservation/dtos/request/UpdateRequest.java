package com.example.airlinereservation.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateRequest {
	@Nullable private String firstName;
	@Nullable private String lastName;
	@Nullable private String Email;
	@Nullable private String phoneNumber;
	@Nullable private String password;
	@Nullable private String newUserName;
	@NotNull private String userName;
	private MultipartFile profileImage;
	@Nullable private String fullName;
	@Nullable private String country;
	@Nullable private String state;
	@Nullable private String postalCode;
	@Nullable private String streetName;
	@Nullable private String streetNumber;
	@Nullable private String houseNumber;

}
