package logging.proxy;

import logging.exception.MissingInterfaceException;

import java.lang.reflect.Proxy;

public class LogReflection {

    public static Object createProxyClass(Object object) {
        Class<?> clazz = object.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            throw new MissingInterfaceException("Класс на основе которого создается proxy не реализует интерфейсы!");
        }
        return Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new LogHandler(object));
    }
}
