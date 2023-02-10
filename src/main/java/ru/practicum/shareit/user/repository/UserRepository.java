package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> getAllUsers();

    User update(User user);

    User create(User user);

    void delete(Integer userId);

    User getUserById(Integer userId);
}
