package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private static final LocalDate DATE_FIRST_FILM = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    public ArrayList<Film> getAll() {
        return new ArrayList<>(filmStorage.getAll());
    }

    public Film create(Film film) throws ValidationException {
        validation(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        validation(film);
        return filmStorage.update(film);
    }

    public Film getById(Long id) throws FilmNotFoundException {
        final Film film = filmStorage.getById(id);
        if (film == null) {
            throw new FilmNotFoundException("Фильм с таким id не найден");
        }
        return film;
    }

    public List<Film> getCountFilms(int count) {
        List<Film> films = filmStorage.getAll();
        films.sort(new ComparatorFilm());
        return films.stream().limit(count).collect(Collectors.toList());
    }

    public Film likeFilm(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        Film film = likeValidation(filmId, userId);
        Set<Long> idLikesFilm = new HashSet<>();
        if (film.getLikesFromUsers() != null) {
            idLikesFilm = film.getLikesFromUsers();
        }
        idLikesFilm.add(userId);
        film.setLikesFromUsers(idLikesFilm);
        return filmStorage.update(film);
    }

    public Film deleteFilm(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        Film film = likeValidation(filmId, userId);
        Set<Long> idLikesFilm = new HashSet<>();
        if (film.getLikesFromUsers() != null) {
            idLikesFilm = film.getLikesFromUsers();
        }
        idLikesFilm.add(userId);
        film.setLikesFromUsers(idLikesFilm);
        return filmStorage.update(film);

    }

    public Film likeValidation(Long filmId, Long userId) throws UserNotFoundException, FilmNotFoundException {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Фильм с таким id не найден");
        } else if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException("Пользователь с таким id не найден");
        }
        return film;
    }

    public void validation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Введено пустое значение имени");
        } else if (film.getDescription().length() > 200) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Введено слишком длинное описание, более 200 символов");
        } else if (film.getDuration() <= 0) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else if (film.getReleaseDate().isBefore(DATE_FIRST_FILM)) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Тогда еще не было фильмов");
        }
    }
}
