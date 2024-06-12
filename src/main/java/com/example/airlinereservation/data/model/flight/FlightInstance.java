package com.example.airlinereservation.data.model.flight;

import com.example.airlinereservation.data.model.aircraft.AirCraft;
import com.example.airlinereservation.data.model.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightInstance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private boolean isFullyBooked;
	@Column(unique = true)
	private String flightNumber;
	private ZonedDateTime departureTime;
	private ZonedDateTime arrivalTime;
	private int baggageAllowance;
	@OneToOne
	private AirCraft airCraft;
	@Enumerated(STRING)
	private FlightStatus status;
	@OneToMany(fetch = FetchType.EAGER)
	private List<FlightSeat> flightSeat;
}
