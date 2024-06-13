package com.example.airlinereservation.services.flightservice;

import com.example.airlinereservation.data.model.*;
import com.example.airlinereservation.data.model.enums.PaymentMethod;
import com.example.airlinereservation.data.model.enums.PaymentStatus;
import com.example.airlinereservation.data.model.enums.Price;
import com.example.airlinereservation.data.model.flight.Flight;
import com.example.airlinereservation.services.userservice.CustomerService;
import com.example.airlinereservation.exceptions.InvalidRequestException;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.airlinereservation.dtos.request.BookingRequest;
import com.example.airlinereservation.dtos.request.CustomerRequest;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookableTest {
	@Autowired
	Bookable bookable;
	BookingRequest bookingRequest;
	Payment payment;
	CustomerRequest passengerRequest;
	@Autowired
	CustomerService passengerService;
	Flight booked;
	@Autowired
	Bookable bookable1;
	
	@BeforeEach void startAllTestWith(){
		payment = new Payment();
		bookingRequest = new BookingRequest();
		passengerRequest = new CustomerRequest();
	}
	
	@Test void testThatANewFlightCanBeCreated(){
		Flight createdFlight = bookable.createNewFlight("ABUJA");
		assertThat(createdFlight).isNotNull();
	}
	
	@SneakyThrows
	@Test void testThatNewFlightIsNotCreatedIfThePreviousFlightIsNotFilledOrFullyBooked(){
		assertThrows(InvalidRequestException.class,
				()-> bookable.createNewFlight("ABUJA"), "Previous Flight Is Not Fully Booked And Is Not Ready To Depart");
	}
	
	@Test void testThatNewFlightIsCreatedIfPreviousFlightIsFullyBooked(){
		Flight createdFlight1 = bookable1.createNewFlight("LAGOS");
		int category = BigInteger.ZERO.intValue();
		for (int index = 0; index < BigInteger.valueOf(20).intValue(); index++) {
			if (category % BigInteger.valueOf(5).intValue() == BigInteger.ZERO.intValue())
				category += BigInteger.valueOf(5).intValue();
			Passenger passenger = new Passenger();
			passenger.setLoggedIn(true);
			bookable1.assignSeatToPassenger(passenger, "LAGOS", category);
		}
		Flight createdFlight2 = bookable1.createNewFlight("LAGOS");
		assertThat(createdFlight2).isNotNull();
		assertThat(bookable1.isNotFilled(createdFlight1)).isFalse();
		assertThat(bookable1.isNotFilled(createdFlight2)).isTrue();
	}
	
	@Test void testThatANewFlightIsNotCreatedIfThePreviousFlightHasNotDeparted(){
	
	}
	
	@Test void checkAvailableFlight_FlightWhichIsNotFullyBooked_IsTheAvailableFlightReturned() throws InvalidRequestException {
	
	}
	
	@Test void testThatIfASectionIsFullyBooked_TheSystemThrowsASeatFullyBookedException_AndTheUserIsPromptedToBookAnotherSection(){
	
	}
	
	@Test void passengerTriesToBookFlightWithAnUndefinedDestination_InvalidRequestExceptionIsThrown(){
	
	}
	
	@Test void passengerTriesToBookFlightWithoutBeingLoggedIn_InvalidRequestExceptionIsThrown(){
	
	}
	
	@Test void testThatPassengerHasToRegister_BeforeTheyAreEligibleToBookAFlight_ElseInvalidRequestExceptionIsThrown(){
	
	}
	
	@SneakyThrows
	@Test
	void checkAvailableFlightTest(){
		Flight availableFlight = bookable1.getAvailableFlight("flight");
		assertNotNull(availableFlight);
	}
	@SneakyThrows
	@Test void testThatPassengerCanBookFlight_AndSeatsWillBeAssignedToThePassenger(){
		passengerService.registerNewCustomer(buildPassenger());
		bookingRequest.setBookingCategory(3);
		booked = bookable.bookFlight(bookingRequest);
//		assertTrue(booked.getAirCraft().getAircraftSeats()[15]);
	}
	
	@SneakyThrows
	@Test void testThatAFlightHasToBeFullyBookedBeforeAnother(){
		passengerService.registerNewCustomer(buildPassenger());
		passengerService.registerNewCustomer(buildPassenger1());
		
		Flight bookedFlight = bookable.bookFlight(getBookingRequest1());
		Flight bookedFlight2 = bookable.bookFlight(getBookingRequest2());
//		assertTrue(bookedFlight.getAirCraft().getAircraftSeats()[10]);
//		assertTrue(bookedFlight2.getAirCraft().getAircraftSeats()[15]);
		System.out.println(booked);
	}
	
	
	@Test void bookFlight_AddPassengerToFlightListOfPassengersTest(){
	
	}
	
	@Test void bookFlight_GenerateFlightFormTest(){
	
	}
	
	private CustomerRequest buildPassenger(){
		return CustomerRequest.builder()
				       .email("alaabdulmalik03@gmail.com")
				       .phoneNumber("07036174617")
				       .firstName("Abdulmalik")
				       .lastName("Alayande")
				       .password("ayanniyi@20")
				       .build();
	}
	
	private CustomerRequest buildPassenger1(){
		return CustomerRequest.builder()
				       .email("alaabdulmalik03@gmail.com")
				       .phoneNumber("07036174617")
				       .firstName("pencil")
				       .lastName("eraser")
				       .password("ayanniyi@20")
				       .build();
	}
	@NotNull
	private BookingRequest getBookingRequest2(){
		Payment payment2 = new Payment();
		payment2.setPaymentMethod(PaymentMethod.CARD);
		payment2.setPrice(Price.ECONOMY_CLASS);
		payment2.setStatus(PaymentStatus.COMPLETED);
		
		BookingRequest bookingRequest2 = new BookingRequest();
		bookingRequest2.setBookingCategory(3);
		bookingRequest2.setPayment(payment2);
		return bookingRequest2;
	}
	
	private BookingRequest getBookingRequest1() {
		Payment payment1 = new Payment();
		payment1.setPaymentMethod(PaymentMethod.CASH);
		payment1.setPrice(Price.BUSINESS_CLASS);
		payment1.setStatus(PaymentStatus.PENDING);
		
		BookingRequest bookingRequest1 = new BookingRequest();
		bookingRequest1.setBookingCategory(2);
		bookingRequest1.setPayment(payment1);
		return bookingRequest1;
	}
}