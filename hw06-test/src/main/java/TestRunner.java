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
     * @param ta класс, который надо оттестировать.
     */
    public static void runTest(TestAnnotation ta) throws ReflectiveOperationException {
        List<Message> messages = new ArrayList<>();
        invokeAllMethods(messages, ta, 2, 0);
        messages.forEach(Message::showMessage);
        showStatisticTest(messages);
    }

    /**
     * Метод для выполнения всех методов вместе, класса {@link TestAnnotation}.
     */
    private static void invokeAllMethods(List<Message> messages, TestAnnotation ta, Object... args)
                                                                                throws ReflectiveOperationException {
        boolean isError;
        List<Method> methodsBefore = getMethodsBeforeAndAfterAnnotation(ta, Before.class);
        List<Method> methodsAfter = getMethodsBeforeAndAfterAnnotation(ta, After.class);
        List<Method> methodsTest = getMethodsBeforeAndAfterAnnotation(ta, Test.class);
        for (Method m : methodsTest) {
            TestAnnotation testAnnotation = newTestAnnotation(ta);
            invokeAfterAndBeforeMethods(methodsBefore, testAnnotation);
            isError = invokeTestMethods(testAnnotation, m, args);
            messages.add(new Message(ta.getClass(), m, isError));
            invokeAfterAndBeforeMethods(methodsAfter, testAnnotation);
        }
    }

    /**
     * Получение нового экземпляра {@link TestAnnotation}.
     */
    private static TestAnnotation newTestAnnotation(TestAnnotation ta) throws ReflectiveOperationException {
        Class<?> clazz = Class.forName(ta.getClass().getName());
        return (TestAnnotation) clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * Выполнение методов помеченных аннотацией @Test и получение информации об успешности теста.
     */
    private static boolean invokeTestMethods(TestAnnotation testAnnotation, Method method, Object[] args)
                                                                                throws ReflectiveOperationException {
        boolean isError = false;
        try {
            method.invoke(testAnnotation, args);
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
    private static void invokeAfterAndBeforeMethods(List<Method> methods, TestAnnotation testAnnotation)
                                                            throws InvocationTargetException, IllegalAccessException {
        for (Method m : methods) {
            m.invoke(testAnnotation);
        }
    }

    /**
     * Метод для получения методов помеченных аннотациями: {@link Before}, {@link After}.
     */
    private static List<Method> getMethodsBeforeAndAfterAnnotation(TestAnnotation ta,
                                                                   Class<? extends Annotation> clazz) {
        Method[] methods = ta.getClass().getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method m : methods) {
            Annotation t = m.getAnnotation(clazz);
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
