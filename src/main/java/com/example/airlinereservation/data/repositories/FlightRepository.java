package com.example.airlinereservation.data.repositories;

import com.example.airlinereservation.data.model.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource(path = "flights")
public interface FlightRepository extends JpaRepository<Flight, String> {
	
	
	@Query("""
        select f from Flight f
        where f.arrivalAirport.airportName = :arrivalAirportName
        and f.departureAirport.airportName = :departureAirportName
        """)
	Optional<Flight> findByArrivalAndDepartureAirport(@Param("arrivalAirportName") String arrivalAirportName,
	                                                          @Param("departureAirportName") String departureAirportName);
	@Query("""
		select f from Flight f
		where f.arrivalCity = :arrivalCity
		and f.departureCity = :departureCity
		""")
	Optional<Flight> findByLocation(String arrivalCity, String departureCity);
	boolean existsByArrivalCityAndDepartureCity(String arrivalCity, String departureCity);
}
