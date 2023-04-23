package com.example.telegrambot.spi;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T, I> {

    Optional<T> get(String id);

    Collection<T> getAll();
//
//    Optional<I> save(T t);
//
    void update(T t);
//
//    void delete(T t);
}