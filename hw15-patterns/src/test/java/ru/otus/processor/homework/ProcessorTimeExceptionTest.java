package ru.otus.processor.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ProcessorTimeExceptionTest {
    private static final String MESSAGE_EXCEPTION = "Ошика в каждую четную секунда!";

    private final Message message = new Message.Builder(1)
            .field12("12")
            .build();
    private Processor processor;


    @DisplayName("Когда секунда не четная и исключения не выбрасывается")
    @Test
    void whenSecondNotEventThanNotTestException() {
        processor = new ProcessorTimeException(
                () -> LocalDateTime.of(2022, 9, 10, 10, 15, 3),
                () -> new TestException(MESSAGE_EXCEPTION)

        );

        Assertions.assertThat(processor.process(message)).isEqualTo(message);
    }

    @DisplayName("Когда секунда четная и выбрасывается исключение")
    @Test
    void whenSecondEventThanTestException() {
        processor = new ProcessorTimeException(
                () -> LocalDateTime.of(2022, 9, 10, 10, 15, 4),
                () -> new TestException(MESSAGE_EXCEPTION)

        );

        assertThatExceptionOfType(TestException.class)
                .isThrownBy(() -> processor.process(message))
                .withMessage(MESSAGE_EXCEPTION);
    }

    private static class TestException extends RuntimeException {

        public TestException(String message) {
            super(message);
        }
    }
}