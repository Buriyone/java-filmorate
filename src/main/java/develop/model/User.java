package develop.model;

import javax.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
	private int id;
	private final String login;
	private final String name;
	@Email(message = "некорректный email.")
	private final String email;
	private final LocalDate birthday;
}
