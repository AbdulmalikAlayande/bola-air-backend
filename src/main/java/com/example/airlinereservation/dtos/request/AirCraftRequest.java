package com.example.airlinereservation.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AirCraftRequest {
	@NotBlank
	private String airCraftName;
	@NotBlank
	private String model;
	@NotBlank
	private LocalDate datePurchased;
	private String location;
}
