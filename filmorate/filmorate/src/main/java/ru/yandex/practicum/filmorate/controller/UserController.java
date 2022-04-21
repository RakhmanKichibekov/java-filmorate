package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        post(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) throws ValidationException {
        post(user);
        return user;
    }

    public void post(User user) throws ValidationException {
        if ((user.getEmail() == null || user.getEmail().isBlank()) && !(user.getEmail().contains("@"))) {
            log.warn("Введено пустое значение email или забыт символ - @ ");
            throw new ValidationException("Введено пустое значение email или забыт символ - @ ");
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Введено пустой логин ");
            throw new ValidationException("Введено пустой логин ");
        } else if (user.getDisplayName() == null || user.getDisplayName().isBlank()) {
            log.info("Вы не задали имя, делаем его как логин ");
            user.setDisplayName(user.getLogin());
        } else if (user.getBirthday().isAfter(LocalDateTime.now())) {
            log.warn("Вы из будущего ? ");
            throw new ValidationException("Вы из будущего ? ");
        }
        users.put(user.getId(), user);
        log.info("Добавлен пользователь ");

    }
}
