package develop.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
public class Mpa {
    private int id;
    @NotBlank(message = "Ошибка валидации. Причина: имя не может быть пустым и состоять из пробелов.")
    private String name;
}
