package ru.otus.listener.homework;

@FunctionalInterface
public interface Prototype<T> {

    T clone();
}
