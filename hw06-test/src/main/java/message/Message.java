package message;

import java.lang.reflect.Method;

/**
 * Информация о результате тестирования.
 */
public class Message {
    private final Class<?> clazz;
    private final Method method;
    private final Boolean isError;

    public Message(Class<?> clazz, Method method, Boolean isError) {
        this.clazz = clazz;
        this.method = method;
        this.isError = isError;
    }

    public Boolean getError() {
        return isError;
    }

    public void showMessage() {
        String errorText = "выполнен с ошибками";
        String correctText = "выполнен без ошибок";
        String message = String.format("Метод - %s, класса - %s, %s !!!",
                method.getName(),
                clazz.getName(),
                isError ? errorText : correctText);
        System.out.println(message);
    }
}
