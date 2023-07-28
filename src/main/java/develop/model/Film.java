package develop.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
	@NotNull(message = "Ошибка валидации. Причина: название фильма должно быть объявлено.")
	@NotEmpty(message = "Ошибка валидации. Причина: название не может быть пустым.")
	private String name;
	@NotNull(message = "Ошибка валидации. Причина: описание фильма должно быть объявлено.")
	@Length(max = 200, message = "Ошибка валидации. Причина: максимальная длина описания — 200 символов.")
	private String description;
	@NotNull(message = "Ошибка валидации. Причина: дата релиза фильма должна быть объявлена.")
	private LocalDate releaseDate;
	@NotNull(message = "Ошибка валидации. Причина: время продолжительности фильма должно быть объявлено.")
	@Positive(message = "Ошибка валидации. Причина: продолжительность фильма должна быть положительной.")
	private int duration;
	private int id;
}
