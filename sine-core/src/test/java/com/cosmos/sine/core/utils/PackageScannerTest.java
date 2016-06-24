package com.cosmos.sine.core.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test case for {@link PackageScanner}.
 *
 * @author BSD
 */
public class PackageScannerTest {

    private PackageScanner scanner;
    private String packageOnDirectory;
    private String packageInJar;

    @Before
    public void setUp() {
        this.scanner = new PackageScanner();

        this.packageOnDirectory = "com.cosmos.sine.core";
        this.packageInJar = "org.junit";
    }

    @Test
    public void testScan() {
        try {
            // scan on file directory
            List<Class<?>> scanClazzList = scanner.scan(packageOnDirectory);
            assertTrue("no class found on directory", scanClazzList.size() > 0);

            // scan in jar
            scanClazzList = scanner.scan(packageInJar);
            assertTrue("no class found in jar file", scanClazzList.size() > 0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("class not found!");
        }
    }

}