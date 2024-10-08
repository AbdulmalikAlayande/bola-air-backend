package app.bola.flywell.services.notifications.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Sender {
	
	@JsonProperty("name")
	private String name;
	@JsonProperty("email")
	private String email;
}
