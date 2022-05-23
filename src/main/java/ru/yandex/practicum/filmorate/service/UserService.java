package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;


    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public ArrayList<User> getAll() {
        return new ArrayList<>(userStorage.getAll());
    }

    public User create(User user) {
        validation(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validation(user);
        return userStorage.update(user);
    }

    public User getById(Long id) {
        final User user = userStorage.getById(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        return user;
    }

    public User addFriends(Long userId, Long friendId) throws UserNotFoundException {
        final User user = userStorage.getById(userId);
        final User friend = userStorage.getById(friendId);
        if (user == null) {
            throw new UserNotFoundException("Фильма с таким id не существует");
        } else if (friend == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else {
            Set<Long> idFriendsForUser = new HashSet<>();
            if (user.getFriends() != null) {
                idFriendsForUser = user.getFriends();
            }
            idFriendsForUser.add(friendId);
            user.setFriends(idFriendsForUser);

            Set<Long> idFriendsForFriend = new HashSet<>();
            if (friend.getFriends() != null) {
                idFriendsForFriend = friend.getFriends();
            }
            idFriendsForFriend.add(userId);
            friend.setFriends(idFriendsForFriend);
            userStorage.update(friend);
            return userStorage.update(user);
        }
    }

    public User deleteFriends(Long userId, Long friendId) throws UserNotFoundException {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user == null) {
            throw new UserNotFoundException("Фильма с таким id не существует");
        } else if (friend == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else {
            Set<Long> idUSer = new HashSet<>();
            if (user.getFriends() != null) {
                idUSer = user.getFriends();
            }
            idUSer.remove(friendId);
            user.setFriends(idUSer);

            Set<Long> idFriend = new HashSet<>();
            if (friend.getFriends() != null) {
                idFriend = friend.getFriends();
            }
            idFriend.remove(userId);
            friend.setFriends(idFriend);
            userStorage.update(friend);
            return userStorage.update(user);
        }
    }

    public List<User> getFriends(Long userId) throws UserNotFoundException {
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else {
            List<User> friendsList = new ArrayList<>();
            if (user.getFriends() != null) {
                for (Long id : user.getFriends()) {
                    friendsList.add(userStorage.getById(id));
                }
            }
            return friendsList;
        }
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) throws UserNotFoundException {
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherUserId);

        if (user == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else if (otherUser == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else {
            if (user.getFriends() == null) {
                if (otherUser.getFriends() == null) {
                    return List.of();
                } else {
                    for (Long id : otherUser.getFriends()) {
                        commonFriends.add(userStorage.getById(id));
                    }
                    return commonFriends;
                }
            } else {
                Set<Long> duplicateFriendsUser = new HashSet<>(user.getFriends());
                duplicateFriendsUser.retainAll(otherUser.getFriends());

                for (Long id : duplicateFriendsUser) {
                    commonFriends.add(userStorage.getById(id));
                }
                return commonFriends;
            }
        }
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
