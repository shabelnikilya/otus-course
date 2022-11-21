package ru.otus.jdbc.mapper;

/**
 * @author Shabelnik Ilya (ishabelnik@unislabs.com)
 */
public interface EntitySQLMetaData {

    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();
}
