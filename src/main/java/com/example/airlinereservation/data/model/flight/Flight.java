package com.example.airlinereservation.data.model.flight;

import com.example.airlinereservation.data.model.Airport;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
public class Flight {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private long durationInHrs;
	@NotBlank
	private String airline;
	private String arrivalCity;
	private String departureCity;
	private String displayImageName;
	@OneToOne(cascade = ALL)
	private Airport departureAirport;
	@OneToOne(cascade = ALL)
	private Airport arrivalAirport;
	@OneToMany(cascade = ALL, fetch = EAGER, orphanRemoval = true)
	private List<FlightInstance> flightInstances;
}
