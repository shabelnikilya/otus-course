package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.service.JdbcTypeService;
import ru.otus.jdbc.mapper.service.JdbcTypeServiceImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final JdbcTypeService jdbcTypeService = new JdbcTypeServiceImpl();
    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    Object[] args = getObjects(rs);
                    return entityClassMetaData.getConstructor().newInstance(args); // todo
                }
                return null;
            } catch (Exception e) {
                throw new UnsupportedOperationException();
            }
        });
    }

    private Object[] getObjects(ResultSet rs) throws SQLException {
        Object[] args = new Object[rs.getMetaData().getColumnCount()];
        int i = 0;
        for (Class<?> c : entityClassMetaData.getConstructor().getParameterTypes()) {
            args[i] = jdbcTypeService.getJdbcMethod(c.getSimpleName()).apply(rs, i + 1);
            i++;
        }
        return args;
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            try {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    Object[] args = getObjects(rs);
                    results.add(entityClassMetaData.getConstructor().newInstance(args));
                }
                return results;
            } catch (Exception e) {
                throw new UnsupportedOperationException();
            }
        }).orElseThrow(RuntimeException::new);
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> objects = getParams(client);
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), objects);
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> objects = getParams(client);
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), objects);
    }

    private List<Object> getParams(T client) {
        return jdbcTypeService.getParams(client.getClass()).apply(client);
    }
}
