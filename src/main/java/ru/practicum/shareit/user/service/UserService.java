package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto update(UserDto userDto, Integer userId);

    UserDto create(UserDto userDto);

    void delete(Integer id);

    UserDto getUserById(Integer id);
}
