package logging.proxy;

import logging.annotation.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LogHandler implements InvocationHandler {
    private final Object proxyObject;

    public LogHandler(Object proxyObject) {
        this.proxyObject = proxyObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method originalMethod = null;
        int argsLength = args == null ? 0 : args.length;
        Method[] methods = proxyObject.getClass().getDeclaredMethods();
        for (Method m : methods) {
            int mLength = m.getParameterCount();
            if (m.getName().equals(method.getName()) && mLength == argsLength) {
                originalMethod = m;
            }
        }
        Annotation annotation = originalMethod == null ? null : originalMethod.getAnnotation(Log.class);
        if (annotation != null && argsLength != 0) {
            Arrays.asList(args).forEach(
                    arg -> System.out.println("Executed method: " + method.getName() + ", param: " + arg)
            );
        }
        return method.invoke(proxyObject, args);
    }
}
