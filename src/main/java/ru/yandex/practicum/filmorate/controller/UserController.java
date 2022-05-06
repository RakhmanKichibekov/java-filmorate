package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public ArrayList<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        log.info("Добавлен пользователь");
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        log.info("Добавлен пользователь");
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) throws UserNotFoundException {
        return userService.getById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addToFriends(@PathVariable Long userId, @PathVariable Long friendId) throws UserNotFoundException {
        return userService.addFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFromFriends(@PathVariable Long userId, @PathVariable Long friendId) throws UserNotFoundException {
        log.debug("Входящий запрос на удаление из друзей пользователя с id = {} у пользователя c id = {}",
                friendId, userId);
        return userService.deleteFriends(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsForUser(@PathVariable Long userId) throws UserNotFoundException {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherUserId)
            throws UserNotFoundException {
        return userService.getCommonFriends(userId, otherUserId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerError(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

}
