package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Film {
    private String name;
    private Long id;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private static AtomicLong counter = new AtomicLong(0);

    public static void setCounter(AtomicLong counter) {
        Film.counter = counter;
    }

    public static Long setIdCounter() {
        return counter.incrementAndGet();
    }
}
