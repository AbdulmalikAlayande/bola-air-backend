package app.bola.flywell.dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirCraftResponse {
	
	private String hangerId;
	private String airCraftName;
	private String model;
	private String message;
}
