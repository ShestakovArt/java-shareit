package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, Long userId) {
        userDto.setId(userId);
        User userOld = findById(userId);
        User userNew = userMapper.mapToUser(userDto);
        userNew = updateUserData(userNew, userOld);
        return userMapper.mapToUserDto(userNew);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.mapToUser(userDto);
        return userMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found User with Id: " + userId));
        return userMapper.mapToUserDto(user);
    }

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id: %s не найден", userId)));
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

    private void checkUniqueEmail(User user) {
        List<String> userEmails = userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (userEmails.contains(user.getEmail())) {
            throw new ValidationException(
                    "Пользователь с email = " + user.getEmail() + " уже существует");
        }
    }
}
