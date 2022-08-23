package logging;

import logging.log.TestLogging;
import logging.log.TestLoggingImpl;
import logging.proxy.LogReflection;
import logging.test.TestClass;

public class Main {
    public static void main(String[] args) {
        TestLogging testLogging = new TestLoggingImpl();

        TestClass testClass = new TestClass(testLogging);
        TestClass testClassProxy = new TestClass(LogReflection.createProxyClass(testLogging));

        System.out.println("Методы без добавления логирования параметров методов:");
        testClass.action();
        System.out.println("-----------------------------------");
        System.out.println("Методы с добавлением логирования параметров методов:");
        testClassProxy.action();
    }
}
