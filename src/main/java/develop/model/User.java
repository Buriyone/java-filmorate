package develop.model;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
	@NotNull(message = "Ошибка валидации. Причина: логин должен быть объявлен.")
	@NotEmpty(message = "Ошибка валидации. Причина: логин не может быть пустым.")
	private String login;
	private String name;
	@NotNull(message = "Ошибка валидации. Причина: электронная почта должна быть объявлена.")
	@NotEmpty(message = "Ошибка валидации. Причина: электронная почта должна быть заполнена.")
	@Email(message = "некорректный email.")
	private String email;
	@NotNull(message = "Ошибка валидации. Причина: дата рождения должна быть объявлена.")
	@PastOrPresent(message = "Ошибка валидации. Причина: дата рождения не может быть в будущем.")
	private LocalDate birthday;
	private int id;
}
