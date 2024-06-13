package com.example.airlinereservation.data.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Airport {
	@Id
	@GeneratedValue(strategy = UUID)
	private String id;
	private String name;
	private String icaoCode;
	private String iataCode;
	private String isoCountryCode;
	private String address;
	private Long longitude;
	private Long latitude;
}
