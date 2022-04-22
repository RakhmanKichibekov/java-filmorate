package ru.yandex.practicum.filmorate.controller;

import jdk.jfr.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

@Slf4j
@RestController
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping("/films")
    public ArrayList<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        if(film.getName()==null || film.getName().isBlank()) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Введено пустое значение имени");
        } else if (film.getDescription().length() > 200) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Введено слишком длинное описание, более 200 символов");
        } else if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else if (film.getRelease().isBefore(LocalDate.of(1895, 12, 28))){
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Тогда еще не было фильмов");
        } else {
            Long newID = Film.setIdCounter();
            film.setId(newID);
            films.put(newID, film);
            log.info("Добавлен фильм ");
            return film;
        }
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws ValidationException {
        if(film.getName()==null || film.getName().isBlank()) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Введено пустое значение имени");
        } else if (film.getDescription().length() > 200) {
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Введено слишком длинное описание, более 200 символов");
        } else if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.warn("Продолжительность фильма не может быть отрицательной");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else if (film.getRelease().isBefore(LocalDate.of(1895, 12, 28))){
            log.warn("Произошла ошибка валидации при создании фильма");
            throw new ValidationException("Тогда еще не было фильмов");
        } else {
            films.put(film.getId(), film);
            log.info("Добавлен фильм");
            return film;
        }
    }
}
