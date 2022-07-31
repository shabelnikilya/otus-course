package test;

import annotations.After;
import annotations.Before;
import annotations.Test;
import exception.TestAnnotationException;

/**
 * Класс-тест.
 */
public class TestAnnotation {

    private int a = 1;

    private int b = 1;

    public TestAnnotation() {
        System.out.println("Инициализация нового объекта!!!");
    }

    @Before
    public void before() {
        System.out.println("Метод с аннотацией @Before");
    }

    @Test
    public int sum() throws IllegalAccessException {
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
    public int div() throws TestAnnotationException {
        System.out.println("Метод с аннатацией @Test");
        if (b == 0) {
            throw new TestAnnotationException("Тест метода div не пройден!");
        }
        return a / b;
    }

    @Test
    public int diff() {
        System.out.println("Метод с аннатацией @Test");
        return a - b;
    }

    @After
    public void after() {
        System.out.println("Метод с аннотацией @After");
    }
}
