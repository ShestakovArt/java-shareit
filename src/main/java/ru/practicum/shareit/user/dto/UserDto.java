package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.markers.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @NotBlank(groups = {Marker.Create.class},
            message = "Имя пользователя не может быть пустым")
    String name;
    @NotBlank(groups = {Marker.Create.class},
            message = "Email не может быть пустым")
    @Email(groups = {Marker.Create.class, Marker.Update.class},
            message = "Указан некорректный email")
    String email;
}