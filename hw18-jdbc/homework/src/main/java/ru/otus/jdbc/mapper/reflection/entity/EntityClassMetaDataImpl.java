package ru.otus.jdbc.mapper.reflection.entity;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.reflection.entity.annotation.Id;
import ru.otus.jdbc.mapper.reflection.entity.annotation.InitEntity;
import ru.otus.jdbc.mapper.reflection.entity.exception.NotFoundIdFieldException;
import ru.otus.jdbc.mapper.reflection.entity.exception.NotFoundInitConstructorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.constructor = initFullConstructor();
        this.idField = initIdField();
        this.allFields = initAllFields();
        this.fieldsWithoutId = initFieldsWithoutId();
    }

    private Constructor<T> initFullConstructor() {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(InitEntity.class)) {
                return constructor;
            }
        }
        throw new NotFoundInitConstructorException(
                String.format(
                        "В классе - %s, отсутствует конструктор дли инициализации(отсутсвует аннотация - %s)!",
                        clazz.getSimpleName(), InitEntity.class.getSimpleName()
                )
        );
    }

    private Field initIdField() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new NotFoundIdFieldException("Отсутствует поле помеченное как идентификатор для БД");
    }


    private List<Field> initAllFields() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    private List<Field> initFieldsWithoutId() {
        List<Field> resultFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }
            resultFields.add(field);
        }
        return resultFields;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
