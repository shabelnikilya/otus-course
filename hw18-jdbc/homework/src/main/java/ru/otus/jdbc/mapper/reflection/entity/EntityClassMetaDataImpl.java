package ru.otus.jdbc.mapper.reflection.entity;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.reflection.entity.annotation.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private T t;

    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        List<Field> fields = getAllFields();
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (fields.size() == constructor.getParameterCount()) {
                return constructor;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Field getIdField() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation annotation = field.getAnnotation(Id.class); // todo exception если несколько полей помечено этим методом
            if (annotation != null) {
                return field;
            }
        }
        throw new UnsupportedOperationException("Отсутствует поле помеченное как идентификатор для БД");
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> resultFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                continue;
            }
            resultFields.add(field);
        }
        return resultFields;
    }
}
