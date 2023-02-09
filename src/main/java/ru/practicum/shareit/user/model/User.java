package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @Email(message = "Email должен содержать '@'")
    @NotBlank(message = "Email не может быть пустым")
    String email;
}
