package ru.otus.jdbc.mapper.service;

import ru.otus.model.Client;
import ru.otus.model.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;


public class JdbcTypeServiceImpl implements JdbcTypeService {

    private final Map<String, BiFunction<ResultSet, Integer, Object>> jdbcResults = new HashMap<>();
    private final Map<Class<?>, Function<Object, List<Object>>> params = new HashMap<>();

    public JdbcTypeServiceImpl() {
        jdbcResults.put("String", this::getString);
        jdbcResults.put("Integer", this::getInt);
        jdbcResults.put("Long", this::getLong);
        params.put(Client.class, c -> {
            Client client = (Client) c;
            List<Object> params = new ArrayList<>();
            params.add(client.getName());
            if (client.getId() != null) {
                params.add(client.getId());
            }
            return params;
        });
        params.put(Manager.class, m -> {
            Manager manager = (Manager) m;
            List<Object> params = new ArrayList<>();
            params.add(manager.getLabel());
            params.add(manager.getParam1());
            if (manager.getNo() != null) {
                params.add(manager.getNo());
            }
            return params;
        });
    }

    private String getString(ResultSet r, Integer v) {
        try {
            return r.getString(v);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getInt(ResultSet r, Integer v) {
        try {
            return r.getInt(v);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private long getLong(ResultSet r, Integer v) {
        try {
            return r.getLong(v);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTypeClassAndJdbcMethod(String simpleClassName, BiFunction<ResultSet, Integer, Object> biFunction) {
        jdbcResults.put(simpleClassName, biFunction);
    }

    @Override
    public BiFunction<ResultSet, Integer, Object> getJdbcMethod(String simpleClassName) {
        return jdbcResults.get(simpleClassName);
    }

    @Override
    public void addModel(Class<?> clazz, Function<Object, List<Object>> function) {
        params.put(clazz, function);
    }

    @Override
    public Function<Object, List<Object>> getParams(Class<?> clazz) {
        return params.get(clazz);
    }
}
