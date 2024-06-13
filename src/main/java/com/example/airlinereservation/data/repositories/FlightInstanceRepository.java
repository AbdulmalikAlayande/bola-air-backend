package com.example.airlinereservation.data.repositories;

import com.example.airlinereservation.data.model.enums.FlightStatus;
import com.example.airlinereservation.data.model.flight.FlightInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightInstanceRepository extends JpaRepository<FlightInstance, String> {
	
	List<FlightInstance> findByStatus(FlightStatus status);
	
	@Query("""
			SELECT fi FROM FlightInstance fi
			WHERE fi.departureTime <= :currentDateTime
			AND fi.status = 'EN_ROUTE'
			ORDER BY fi.departureTime DESC
			""")
	Optional<FlightInstance> findLastMovedFlight(@Param("currentDateTime") ZonedDateTime currentDateTime);
	
	
}
