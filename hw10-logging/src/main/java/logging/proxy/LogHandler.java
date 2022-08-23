package logging.proxy;

import logging.annotation.Log;
import logging.log.TestLogging;
import logging.proxy.meta.MetadataMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class LogHandler implements InvocationHandler {
    private final static Class<Log> LOG_ANNOTATION = Log.class;

    private final TestLogging proxyObject;

    private final Set<MetadataMethod> logMethods = new HashSet<>();

    public LogHandler(TestLogging proxyObject) {
        validateObject(proxyObject);
        this.proxyObject = proxyObject;
        loadLogMethods(proxyObject);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int argsLength = args == null ? 0 : args.length;
        if (argsLength == 0) {
            return method.invoke(proxyObject, args);
        }
        MetadataMethod metadataMethod = MetadataMethod.of(method.getName(), Arrays.asList(method.getParameterTypes()));
        if (logMethods.contains(metadataMethod)) {
            Arrays.asList(args).forEach(
                    arg -> System.out.println("Executed method: " + method.getName() + ", param: " + arg)
            );
        }
        return method.invoke(proxyObject, args);
    }

    private static void validateObject(TestLogging proxyObject) {
        if (proxyObject == null) {
            throw new IllegalArgumentException("Класс, на основе которого строится proxy объект не может быть null!");
        }
    }

    private void loadLogMethods(TestLogging proxyObject) {
        Method[] methods = proxyObject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Annotation annotation = method.getAnnotation(LOG_ANNOTATION);
            if (annotation != null) {
                logMethods.add(MetadataMethod.of(method.getName(), Arrays.asList(method.getParameterTypes())));
            }
        }
    }
}
