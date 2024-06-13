package com.example.airlinereservation.services;

import com.example.airlinereservation.services.flightformservice.FlightFormService;
import com.example.airlinereservation.services.flightservice.Bookable;
import com.example.airlinereservation.services.userservice.CustomerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.dtos.request.BookingRequest;
import com.example.airlinereservation.dtos.request.FlightFormRequest;
import com.example.airlinereservation.dtos.request.CustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FlightFormServiceTest {
	FlightFormService flightFormService;
	@Autowired
	Bookable bookable;
	BookingRequest bookingRequest;
	CustomerService passengerService;
	CustomerRequest passengerRequest;
	
	FlightFormRequest flightFormRequest;
	@SneakyThrows
	@BeforeEach void startAllTestWith(){
		passengerRequest = CustomerRequest.builder()
 				                   .email("alaabdulmalik03@gmail.com")
				                   .firstName("Abdulmalik")
				                   .lastName("Alayande")
				                   .password("ayanniyi@20")
				                   .phoneNumber("07036174617")
				                   .build();
		passengerService.registerNewCustomer(passengerRequest);
		bookingRequest = BookingRequest.builder()
				                .bookingCategory(1)
				                .passengerUsername("dende")
				                .build();
		Flight bookedFlight = bookable.bookFlight(bookingRequest);
		flightFormRequest = FlightFormRequest.builder()
				                    .flight(bookedFlight)
//				                    .passenger(passengerService.findAPassengerByUserName(passengerRequest.getUserName()))
				                    .build();
		flightFormService.save(flightFormRequest);
	}
}
