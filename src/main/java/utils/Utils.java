package utils;

import annotation.HasToBeIgnoredForAssertion;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Utils {
    private Utils() {
    }

    public static <T> T prepareForAssertion(T object) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        T clone = reflectionCloner(object);
        Field[] fields = clone.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(HasToBeIgnoredForAssertion.class) && field.trySetAccessible()) {
                Object defaultValue = field.getType() == boolean.class ? Boolean.FALSE : null;
                field.set(clone, defaultValue);
            }
        }

        return clone;
    }

    public static <T> T reflectionCloner(final T src) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<? extends T> srcClass = (Class<? extends T>) src.getClass();

        // Create new instance using reflection
        Constructor<? extends T> constructor = srcClass.getDeclaredConstructor();
        if (!constructor.trySetAccessible()) {
            throw new IllegalAccessException("Could not set accessible for constructor: " + constructor.getName());
        }
        T clone = constructor.newInstance();

        // Copy properties from original instance to the clone
        for (Field field : srcClass.getDeclaredFields()) {
            if (field.trySetAccessible()) {
                field.set(clone, field.get(src));
            }
        }

        return clone;
    }
}
