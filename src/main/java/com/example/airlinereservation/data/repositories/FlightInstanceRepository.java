package com.example.airlinereservation.data.repositories;

import com.example.airlinereservation.data.model.enums.FlightStatus;
import com.example.airlinereservation.data.model.flight.FlightInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FlightInstanceRepository extends JpaRepository<FlightInstance, String> {
	
	@Query(
       """
       select f from FlightInstance f
       where f.status = :status
       and f.arrivalTime = :arrivalTime
       and f.departureTime = :departureTime
       """
	)
	List<FlightInstance> findAvailableInstances(@Param("status") FlightStatus status,
	                                               @Param("arrivalTime") LocalDate arrivalTime,
	                                               @Param("departureTime") LocalDate departureTime);
	List<FlightInstance> findByStatus(FlightStatus status);
}
