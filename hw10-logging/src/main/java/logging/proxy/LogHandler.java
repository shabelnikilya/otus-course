package logging.proxy;

import logging.annotation.Log;
import logging.log.TestLogging;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class LogHandler implements InvocationHandler {

    private final TestLogging proxyObject;

    private final Map<Class<?>, List<Method>> logMethods = new HashMap<>();

    public LogHandler(TestLogging proxyObject) {
        this.proxyObject = proxyObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int argsLength = args == null ? 0 : args.length;
        if (argsLength == 0) {
            return method.invoke(proxyObject, args);
        }
        putLogMethod();
        Method m = getOriginalMethod(method, argsLength);
        if (m != null) {
            Arrays.asList(args).forEach(
                    arg -> System.out.println("Executed method: " + method.getName() + ", param: " + arg)
            );
        }
        return method.invoke(proxyObject, args);
    }

    private Method getOriginalMethod(Method method, int argsLength) {
        List<Method> methodsClass = logMethods.get(proxyObject.getClass());
        for (Method m : methodsClass) {
            int mLength = m.getParameterCount();
            if (m.getName().equals(method.getName()) && mLength == argsLength) {
                return m;
            }
        }
        return null;
    }

    private void putLogMethod() {
        if (!logMethods.containsKey(proxyObject.getClass())) {
            Method[] methods = proxyObject.getClass().getDeclaredMethods();
            List<Method> methodsWithLogAnnotation = new ArrayList<>();
            for (Method method : methods) {
                Annotation annotation = method.getAnnotation(Log.class);
                if (annotation != null) {
                    methodsWithLogAnnotation.add(method);
                }
            }
            logMethods.put(proxyObject.getClass(), methodsWithLogAnnotation);
        }
    }
}
