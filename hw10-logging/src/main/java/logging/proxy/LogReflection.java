package logging.proxy;

import logging.exception.MissingInterfaceException;
import logging.log.TestLogging;

import java.lang.reflect.Proxy;

public class LogReflection {

    public static TestLogging createProxyClass(TestLogging testLogging) {
        Class<?> clazz = testLogging.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            throw new MissingInterfaceException("Класс на основе которого создается proxy не реализует интерфейсы!");
        }
        return (TestLogging) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new LogHandler(testLogging));
    }
}
