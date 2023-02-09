package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImp implements UserRepository{
    private final Map<Integer, User> users = new HashMap<>();
    private static Integer id = 1;
    @Override
    public Collection<User> getAllUsers() {
        log.info("Запрос списка пользователей");
        return users.values();
    }

    @Override
    public User update(User user) {
        findUserToId(user.getId());
        users.put(user.getId(), user);
        log.info("Обновлен пользователь с id = {}", user.getId());
        return user;
    }

    @Override
    public User create(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Создан пользователь с id = {}", user.getId());
        return user;
    }

    @Override
    public void delete(Integer userId) {
        findUserToId(userId);
        users.remove(userId);
        log.info("Пользователь с id = {} удален", userId);
    }

    @Override
    public User getUserById(Integer userId) {
        findUserToId(userId);
        log.info("Поиск пользователя с id = {}", userId);
        return users.get(userId);
    }

    private void findUserToId(Integer userId){
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id = %d не найден", userId));
        }
    }
}
