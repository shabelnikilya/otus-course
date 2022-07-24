package test;

import annotations.After;
import annotations.Before;
import annotations.Test;
import exception.TestAnnotationException;

/**
 * Класс-тест.
 */
public class TestAnnotation {

    public TestAnnotation() {
        System.out.println("Новый класс!");
    }

    @Before
    public void before() {
        System.out.println("Метод с аннотацией @Before");
    }

    @Test
    public int sum(int a, int b) throws IllegalAccessException {
        System.out.println("Метод с аннатацией @Test");
        if (a > 0) {
            throw new IllegalAccessException();
        }
        return a + b;
    }

    /**
     * Исключение для имитации не прохождение теста.
     */
    @Test
    public int div(int a, int b) throws TestAnnotationException {
        if (b == 0) {
            System.out.println("Метод с аннатацией @Test");
            throw new TestAnnotationException("Тест метода div не пройден!");
        }
        System.out.println("Метод с аннатацией @Test");
        return a / b;
    }

    @Test
    public int diff(int a, int b) {
        System.out.println("Метод с аннатацией @Test");
        return a - b;
    }

    @After
    public void after() {
        System.out.println("Метод с аннотацией @After");
    }
}
