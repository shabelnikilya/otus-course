package logging.test;

import logging.log.TestLogging;

public class TestClass {
    private final TestLogging logging;

    public TestClass(TestLogging logging) {
        this.logging = logging;
    }

    public void action() {
        logging.calculation();
        logging.calculation(100);
        logging.calculation(1, 2);
        logging.calculation(10, 20, 30);
    }
}
