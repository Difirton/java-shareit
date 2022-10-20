package ru.practicum.shareit.common.utill;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * This interface contains the operation of copying the values of
 * the fields of the source bean that have a value (do not contain
 * a {@code null} reference) to the target bean.
 * Bean properties are copied using {@link org.springframework.beans.BeanUtils}.
 * <br>
 * <br>Important!
 * <br>When copying from the target bean, only those fields
 * that contained values in the source bean and were not {@code null} will
 * be changed.
 *
 * @author Dmitriy Kruglov
 *
 * @see org.springframework.beans.BeanUtils
 */
public interface NotNullPropertiesCopier<T> {

    /**
     * Accepts a source bean and copies using {@link org.springframework.beans.BeanUtils}
     * all not {@code null} fields to the target bean.
     *
     * @param source bean from which to copy to another bean.
     * @param target bean to which the copy will be made
     *
     * @see org.springframework.beans.BeanUtils
     */
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
