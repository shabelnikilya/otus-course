package exception;

/**
 * Исключение для 'класса теста' {@link test.TestAnnotation}
 */
public class TestAnnotationException extends Exception {

    public TestAnnotationException(String message) {
        super(message);
    }
}
