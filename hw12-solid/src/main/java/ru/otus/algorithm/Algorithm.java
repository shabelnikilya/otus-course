package ru.otus.algorithm;

import ru.otus.models.Amount;

import java.util.List;

public interface Algorithm<T, R extends Amount> {

    List<T> calculationBanknotes(long sum, List<R> values);
}
