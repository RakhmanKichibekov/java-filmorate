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

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        post(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws ValidationException {
        post(film);

        return film;
    }

    private void post(Film film) throws ValidationException {
        if(film.getName()==null || film.getName().isBlank()) {
            log.warn("Введено пустое значение имени ");
            throw new ValidationException("Введено пустое значение имени ");
        } else if (film.getDescription().length() > 200) {
            log.warn("Введено слишком длинное описание, более 200 символов");
            throw new ValidationException("Введено слишком длинное описание, более 200 символов");
        } else if (film.getDuration().isNegative()) {
            log.warn("Продолжительность фильма не может быть отрицательной");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else if (film.getRelease().isBefore(ChronoLocalDateTime.from(LocalDate.of(1895, 12, 28)))){
            log.warn("Тогда еще не было фильмов");
            throw new ValidationException("Тогда еще не было фильмов");
        } else {
            films.put(film.getId(), film);
            log.info("Добавлен фильм ");
        }
    }
}
