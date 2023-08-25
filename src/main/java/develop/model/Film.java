package develop.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder(toBuilder = true)
public class Film {
	private int id;
	@NotBlank(message = "Ошибка валидации. Причина: название не может быть пустым и состоять из пробелов.")
	private String name;
	@Length(max = 200, message = "Ошибка валидации. Причина: максимальная длина описания — 200 символов.")
	private String description;
	private LocalDate releaseDate;
	@Positive(message = "Ошибка валидации. Причина: продолжительность фильма должна быть положительной.")
	private int duration;
	private int rate;
	@NotNull(message = "Ошибка валидации. Причина: MPA не может отсутствовать.")
	private Mpa mpa;
	private LinkedHashSet<Genre> genres;
}
