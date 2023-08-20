package develop.model;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
	@NotBlank(message = "Ошибка валидации. Причина: логин не может быть пустым или содержать пробелы.")
	@Pattern(regexp = "\\S*", message = "Ошибка валидации. Причина: логин не должен содержать пробелы.")
	private String login;
	private String name;
	@NotBlank(message = "Ошибка валидации. Причина: электронная почта не может быть пустой.")
	@Email(message = "некорректный email.")
	private String email;
	@PastOrPresent(message = "Ошибка валидации. Причина: дата рождения не может быть в будущем.")
	private LocalDate birthday;
	private int id;
	private Set<Integer> friends;
}
