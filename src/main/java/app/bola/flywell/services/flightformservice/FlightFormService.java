package app.bola.flywell.services.flightformservice;
import app.bola.flywell.dtos.Request.FlightFormRequest;
import app.bola.flywell.dtos.Request.FlightRequest;
import app.bola.flywell.dtos.Response.FlightFormResponse;

import java.util.List;
import java.util.Optional;

public interface FlightFormService {
	Optional<FlightFormResponse> generateFlightForm(FlightRequest flightRequest);
	Optional<FlightFormResponse> save(FlightFormRequest flightFormRequest);
	Optional<FlightFormResponse> findById(String flightFormId);
	String deleteFlightFormBy(String flightFormId);
	Optional<List<FlightFormResponse>> findAll();
	Optional<List<FlightFormResponse>> findAllByPassengerId(String passengerId);
	Optional<List<FlightFormResponse>> findAllByFlightId(String flightId);
	
}
