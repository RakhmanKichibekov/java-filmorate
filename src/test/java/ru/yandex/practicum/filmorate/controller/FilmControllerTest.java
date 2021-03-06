package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private FilmController filmController;

    @AfterEach
    public void afterEach() {
        Film.setCounter(new AtomicLong(0));
    }

    @Test
    public void createFilmWithoutNameThrowsValidationException() {
        Film film = new Film();
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 2, 26));
        film.setDuration(125);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Введено пустое значение имени", ex.getMessage());
    }

    @Test
    public void createFilmNameConsistsOfSpacesThrowsValidationException() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 2, 26));
        film.setDuration(125);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Введено пустое значение имени", ex.getMessage());
    }

    @Test
    public void createValidFilmNameSuccess() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 2, 26));
        film.setDuration(125);
        filmController.create(film);
        assertEquals(1, filmController.getAll().size());
    }

    @Test
    public void createFilmDescriptionHas201SymbolsThrowsValidationException() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("VeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescription" +
                "VeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescription" +
                "VeeryLongDescription1");
        film.setReleaseDate(LocalDate.of(2000, 2, 26));
        film.setDuration(125);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Введено слишком длинное описание, более 200 символов", ex.getMessage());
    }

    @Test
    public void createFilmDescriptionHas200SymbolsSuccess() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("VeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescription" +
                "VeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescription" +
                "VeeryLongDescription");
        film.setReleaseDate(LocalDate.of(2000, 2, 26));
        film.setDuration(125);
        filmController.create(film);
        assertEquals(1, filmController.getAll().size());
    }

    @Test
    public void createFilmReleaseDate27December1895ThrowsValidationException() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(125);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Тогда еще не было фильмов", ex.getMessage());
    }

    @Test
    public void createFilmReleaseDate29December1895Success() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(125);
        filmController.create(film);
        assertEquals(1, filmController.getAll().size());
    }

    @Test
    public void createFilmDurationIsZeroThrowsValidationException() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1995, 12, 27));
        film.setDuration(0);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Продолжительность фильма не может быть отрицательной", ex.getMessage());
    }

    @Test
    public void createFilmDurationIsNegativeThrowsValidationException() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1995, 12, 27));
        film.setDuration(-150);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Продолжительность фильма не может быть отрицательной", ex.getMessage());
    }

    @Test
    public void updateFilmNewNameIsEmptyThrowsValidationException() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("Description1");
        film1.setReleaseDate(LocalDate.of(1995, 12, 27));
        film1.setDuration(150);
        filmController.create(film1);
        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("");
        film2.setDescription("Description2");
        film2.setReleaseDate(LocalDate.of(1995, 12, 27));
        film2.setDuration(150);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.update(film2));
        assertEquals("Введено пустое значение имени", ex.getMessage());
    }

    @Test
    public void updateFilmNewDescriptionHas201SymbolsThrowsValidationException() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("Description1");
        film1.setReleaseDate(LocalDate.of(1995, 12, 27));
        film1.setDuration(150);
        filmController.create(film1);
        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Name2");
        film2.setDescription("VeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescription" +
                "VeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescriptionVeeryLongDescription" +
                "VeeryLongDescription1");
        film2.setReleaseDate(LocalDate.of(1995, 12, 27));
        film2.setDuration(150);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.update(film2));
        assertEquals("Введено слишком длинное описание, более 200 символов", ex.getMessage());
    }

    @Test
    public void updateFilmNewReleaseDateIs27December1895ThrowsValidationException() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("Description1");
        film1.setReleaseDate(LocalDate.of(1995, 12, 27));
        film1.setDuration(150);
        filmController.create(film1);
        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Name2");
        film2.setDescription("Description2");
        film2.setReleaseDate(LocalDate.of(1895, 12, 27));
        film2.setDuration(150);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.update(film2));
        assertEquals("Тогда еще не было фильмов", ex.getMessage());
    }

    @Test
    public void updateFilmNewDurationIsZeroThrowsValidationException() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("Description1");
        film1.setReleaseDate(LocalDate.of(1995, 12, 27));
        film1.setDuration(150);
        filmController.create(film1);
        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Name2");
        film2.setDescription("Description2");
        film2.setReleaseDate(LocalDate.of(2005, 12, 27));
        film2.setDuration(0);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.update(film2));
        assertEquals("Продолжительность фильма не может быть отрицательной", ex.getMessage());
    }

    @Test
    public void updateFilmSuccess() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("Description1");
        film1.setReleaseDate(LocalDate.of(1995, 12, 27));
        film1.setDuration(150);
        filmController.create(film1);
        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Name2");
        film2.setDescription("Description2");
        film2.setReleaseDate(LocalDate.of(2005, 12, 27));
        film2.setDuration(180);
        filmController.update(film2);
        assertEquals(1, filmController.getAll().size());
        assertEquals(film2, filmController.getAll().get(0));
    }

    @Test
    public void createAndGetTwoFilms() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("Description1");
        film1.setReleaseDate(LocalDate.of(1995, 12, 27));
        film1.setDuration(150);
        filmController.create(film1);
        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDescription("Description2");
        film2.setReleaseDate(LocalDate.of(2005, 12, 27));
        film2.setDuration(180);
        filmController.create(film2);
        assertEquals(2, filmController.getAll().size());
        assertEquals(film1, filmController.getAll().get(0));
        assertEquals(film2, filmController.getAll().get(1));
    }
}
