package ru.practicum.shareit.utill;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface NotNullPropertiesCopier<T> {

    default void copyNotNullProperties(T source, T target) {
        String[] notNullFields = Arrays.stream(source.getClass().getDeclaredFields())
                .filter(f -> {
                    try {
                        f.setAccessible(true);
                        return f.get(source) == null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(Field::getName)
                .toArray(String[]::new);
        BeanUtils.copyProperties(source, target, notNullFields);
    }
}
