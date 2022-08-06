package logging.test;

import logging.log.TestLogging;
import logging.proxy.LogReflection;

/**
 * Класс содержащий proxy объект и вызывающий методы на нем.
 */
public class TestClassWithProxy implements Test {
    private final TestLogging logging;

    public TestClassWithProxy(TestLogging logging) {
        this.logging = (TestLogging) LogReflection.createProxyClass(logging);
    }

    @Override
    public void action() {
        defaultAction(logging);
    }
}
