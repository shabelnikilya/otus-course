package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.util.function.Supplier;

public class ProcessorTimeException implements Processor {
    private DateTimeProvider dateTimeProvider;

    private Supplier<RuntimeException> runtimeExceptionSupplier;
    private Exception exception;

    public ProcessorTimeException(DateTimeProvider dateTimeProvider, Supplier<RuntimeException> exceptionConsumer) {
        this.dateTimeProvider = dateTimeProvider;
        this.runtimeExceptionSupplier = exceptionConsumer;
    }

    @Override
    public Message process(Message message) {
        if (isEvenSecond()) {
            throw runtimeExceptionSupplier.get();
        }
        return message;
    }

    private boolean isEvenSecond() {
        return dateTimeProvider.getLocalDateTime().getSecond() % 2 == 0;
    }
}
