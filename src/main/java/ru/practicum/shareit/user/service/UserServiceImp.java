package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto, Integer userId) {
        validateId(userId);
        userDto.setId(userId);
        User userOld = findUserById(userId);
        User userNew = userMapper.mapToUser(userDto);
        userNew = updateUserData(userNew, userOld);
        return userMapper.mapToUserDto(userRepository.update(userNew));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.mapToUser(userDto);
        checkUniqueEmail(user);
        userDto.setId(userRepository.create(user).getId());
        return userDto;
    }

    @Override
    public void delete(Integer userId) {
        validateId(userId);
        userRepository.delete(userId);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        validateId(userId);
        return userMapper.mapToUserDto(userRepository.getUserById(userId));
    }

    private User updateUserData(User userNew, User userOld) {
        if (userNew.getName() != null) {
            userOld.setName(userNew.getName());
        }
        if (userNew.getEmail() != null) {
            checkUniqueEmail(userNew);
            userOld.setEmail(userNew.getEmail());
        }

        return userOld;
    }

    private User findUserById(Integer userId) {
        validateId(userId);
        User user = userRepository.getUserById(userId);

        return user;
    }

    private void checkUniqueEmail(User user) {
        List<String> userEmails = userRepository.getAllUsers().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (userEmails.contains(user.getEmail())) {
            throw new UserExistsException(
                    String.format("Пользователь с email = '%s' уже существует", user.getEmail()));
        }
    }

    private void validateId(Integer userId) {
        if (userId == null) {
            throw new ValidationException("id не может быть пустым");
        }
        if (userId <= 0) {
            throw new ValidationException("id должен быть больше нуля");
        }
    }
}
