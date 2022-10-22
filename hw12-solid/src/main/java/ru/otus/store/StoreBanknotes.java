package ru.otus.store;

import java.util.List;

public interface StoreBanknotes<T> {

    void add(List<T> moneys);

    List<T> giveBanknotes(long value);

    long balance();
}
