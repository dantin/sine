package com.cosmos.sine.core.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for {@link ClassUtils}.
 *
 * @author BSD
 */
public class ClassUtilsTest {

    @Test
    public void hasDefaultConstructor() throws Exception {
        assertTrue(ClassUtils.hasDefaultConstructor(PackageScanner.class));
    }

}