package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate release;
    private Duration duration;
    private static AtomicLong counter = new AtomicLong(0);

    public static void setCounter(AtomicLong counter) {
        Film.counter = counter;
    }

    public static Long setIdCounter() {
        return counter.incrementAndGet();
    }
}
