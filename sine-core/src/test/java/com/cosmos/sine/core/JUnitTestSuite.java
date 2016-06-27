package com.cosmos.sine.core;

import com.cosmos.sine.core.utils.ClassUtilsTest;
import com.cosmos.sine.core.utils.PackageScannerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for sine-core module.
 *
 * @author BSD
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        PackageScannerTest.class,
        ClassUtilsTest.class
})
public class JUnitTestSuite {
}
