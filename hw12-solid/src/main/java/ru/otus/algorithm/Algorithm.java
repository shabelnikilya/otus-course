package ru.otus.algorithm;

import java.util.List;

public interface Algorithm<T> {

    List<T> calculationBanknotes(long sum, T t);
}
