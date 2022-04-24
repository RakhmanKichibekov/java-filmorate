package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping("/users")
    public ArrayList<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        validation(user);
        Long newID = User.setIdCounter();
        user.setId(newID);
        users.put(newID, user);
        log.info("Добавлен пользователь");
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) throws ValidationException {
        validation(user);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь");
        return user;
    }

    public void validation(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Введено пустое значение email или забыт символ - @");
            throw new ValidationException("Введено пустое значение email или забыт символ - @");
        } else if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Введено пустой логин");
            throw new ValidationException("Введено пустой логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Вы из будущего ?");
            throw new ValidationException("Вы из будущего ?");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
