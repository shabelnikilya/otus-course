import annotations.After;
import annotations.Before;
import annotations.Test;
import exception.TestAnnotationException;
import message.Message;
import test.TestAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Запускалка тестов.
 */
public class TestRunner {

    /**
     * Запуск тестов.
     *
     * @param clazz тестируемый класс.
     */
    public static <T> void runTest(Class<T> clazz) throws ReflectiveOperationException {
        List<Message> messages = new ArrayList<>();
        invokeAllMethods(messages, clazz);
        messages.forEach(Message::showMessage);
        showStatisticTest(messages);
    }

    /**
     * Метод для выполнения всех методов вместе.
     */
    private static <T> void invokeAllMethods(List<Message> messages, Class<T> clazz)
                                                                                throws ReflectiveOperationException {
        boolean isError;
        List<Method> methodsBefore = getMethodsBeforeAndAfterAnnotation(clazz, Before.class);
        List<Method> methodsAfter = getMethodsBeforeAndAfterAnnotation(clazz, After.class);
        List<Method> methodsTest = getMethodsBeforeAndAfterAnnotation(clazz, Test.class);
        for (Method m : methodsTest) {
            T testClass = clazz.getDeclaredConstructor().newInstance();
            invokeAfterAndBeforeMethods(methodsBefore, testClass);
            isError = invokeTestMethods(testClass, m);
            messages.add(new Message(clazz, m, isError));
            invokeAfterAndBeforeMethods(methodsAfter, testClass);
        }
    }

    /**
     * Выполнение методов помеченных аннотацией @Test и получение информации об успешности теста.
     */
    private static <T> boolean invokeTestMethods(T testClass, Method method) throws ReflectiveOperationException {
        boolean isError = false;
        try {
            method.invoke(testClass);
        } catch (InvocationTargetException e) {
            isError = true;
            boolean matcherException = e.getCause() instanceof TestAnnotationException;
            if (!matcherException) {
                e.getCause().printStackTrace();
            }
        }
        return isError;
    }

    /**
     * Метод для выполнения методов с аннотациями @Before и @After класса {@link TestAnnotation}.
     */
    private static <T> void invokeAfterAndBeforeMethods(List<Method> methods, T testClass)
                                                            throws InvocationTargetException, IllegalAccessException {
        for (Method m : methods) {
            m.invoke(testClass);
        }
    }

    /**
     * Метод для получения методов помеченных аннотациями: {@link Before}, {@link After}.
     */
    private static <T> List<Method> getMethodsBeforeAndAfterAnnotation(Class<T> clazz,
                                                                   Class<? extends Annotation> annotationClass) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method m : methods) {
            Annotation t = m.getAnnotation(annotationClass);
            if (Objects.nonNull(t)) {
                methodList.add(m);
            }
        }
        return methodList;
    }

    /**
     * Метод для отображения статистики по тестам.
     */
    private static void showStatisticTest(List<Message> messages) {
        int size = messages.size();
        int howMuchCorrectTest = 0;
        int howMuchErrorTest = 0;
        for (Message m : messages) {
            if (m.getError()) {
                howMuchErrorTest++;
            } else {
                howMuchCorrectTest++;
            }
        }
        System.out.println(
                "Всего выполнено тестов: " + size + System.lineSeparator()
                + "Количество тестов без ошибок: " + howMuchCorrectTest + System.lineSeparator()
                + "Количество тестов выполненных с ошибками: " + howMuchErrorTest + System.lineSeparator()
        );
    }
}
