package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public ArrayList<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        log.info("Добавлен фильм ");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        log.info("Добавлен фильм");
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable Long filmId) throws FilmNotFoundException {
        return filmService.getById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable Long filmId, @PathVariable Long userId) throws
            FilmNotFoundException, UserNotFoundException {
        return filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteFilm(@PathVariable Long filmId, @PathVariable Long userId) throws
            FilmNotFoundException, UserNotFoundException {
        return filmService.deleteFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getCountsFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getCountFilms(count);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFound(final FilmNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerError(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}
