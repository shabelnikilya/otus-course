package ru.otus.jdbc.mapper.service;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface JdbcTypeService {

    void addTypeClassAndJdbcMethod(String simpleClassName, BiFunction<ResultSet, Integer, Object> biFunction);

    BiFunction<ResultSet, Integer, Object> getJdbcMethod(String simpleClassName);

    void addModel(Class<?> clazz, Function<Object, List<Object>> function);

    Function<Object, List<Object>> getParams(Class<?> clazz);
}
