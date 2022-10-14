package ru.otus.jdbc.mapper.reflection.entity;


import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.model.Client;
import ru.otus.model.Manager;

import java.lang.reflect.Field;

/**
 * @author Shabelnik Ilya (ishabelnik@unislabs.com)
 */
public class EntitySqlMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> metaData;

    public EntitySqlMetaDataImpl(EntityClassMetaData<?> metaData) {
        this.metaData = metaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", metaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ?", metaData.getName(), metaData.getIdField().getName()); // todo NPE
    }

    @Override
    public String getInsertSql() {
        StringBuilder insertFields = new StringBuilder();
        StringBuilder insertParams = new StringBuilder();
        String delimiter = "";
        for (Field field : metaData.getFieldsWithoutId()) {
            insertFields.append(delimiter);
            insertParams.append(delimiter);
            insertFields.append(field.getName());
            insertParams.append("?");

            delimiter = ", ";
        }
        return String.format("insert into %s (%s) values (%s)", metaData.getName(), insertFields, insertParams);
    }

    @Override
    public String getUpdateSql() {
        StringBuilder fields = new StringBuilder();
        String delimiter = "";
        for (Field field : metaData.getFieldsWithoutId()) {
            fields.append(delimiter);
            fields.append(String.format("%s = ?", field.getName()));
            delimiter = ", ";
        }


        return String.format("update %s set " + fields + " where %s = ?", metaData.getName(), metaData.getIdField().getName());
    }

    public static void main(String[] args) {
        Client client = new Client("client");
        for (Field f : new EntityClassMetaDataImpl<>(Client.class).getFieldsWithoutId()) {
            System.out.println();
        }
//        for (Class c : new EntityClassMetaDataImpl<>(Manager.class).getConstructor().getParameterTypes()) {
//            System.out.println(c.getT().toString());
//        }
        System.out.println(new EntitySqlMetaDataImpl(new EntityClassMetaDataImpl<>(Client.class)).getInsertSql());
    }
}
