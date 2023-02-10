package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(of = "id")
public class UserDto {
    Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @Email(message = "Email должен содержать '@'")
    @NotBlank(message = "Email не может быть пустым")
    String email;
}
