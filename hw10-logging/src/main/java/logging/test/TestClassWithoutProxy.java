package logging.test;

import logging.log.TestLogging;

/**
 * Класс содержащий оригинальный объект.
 */
public class TestClassWithoutProxy implements Test {
    private final TestLogging logging;

    public TestClassWithoutProxy(TestLogging logging) {
        this.logging = logging;
    }

    @Override
    public void action() {
        defaultAction(logging);
    }
}
