package logging;

import logging.log.TestLogging;
import logging.log.TestLoggingImpl;
import logging.test.Test;
import logging.test.TestClassWithProxy;
import logging.test.TestClassWithoutProxy;

public class Main {
    public static void main(String[] args) {
        TestLogging testLogging = new TestLoggingImpl();

        Test testClassWithoutProxy = new TestClassWithoutProxy(testLogging);
        Test testClassWithProxy = new TestClassWithProxy(testLogging);

        System.out.println("Методы без добавления логирования параметров методов:");
        testClassWithoutProxy.action();
        System.out.println("-----------------------------------");
        System.out.println("Методы с добавлением логирования параметров методов:");
        testClassWithProxy.action();
    }
}
