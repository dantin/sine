package com.cosmos.sine.core.utils;

import java.lang.reflect.Constructor;

/**
 * Class utility.
 *
 * @author BSD
 */
public class ClassUtils {

    private ClassUtils() {
        // do nothing
    }

    /**
     * Check whether a class has a default constructor.
     *
     * @param clazz class
     * @return true if a class has a default constructor, false otherwise
     */
    public static boolean hasDefaultConstructor(Class<?> clazz) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (!constructor.isVarArgs()) {
                return true;
            }
        }

        return false;
    }
}
