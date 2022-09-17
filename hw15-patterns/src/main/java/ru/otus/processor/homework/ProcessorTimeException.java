package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.util.function.Supplier;

public class ProcessorTimeException implements Processor {
    private final DateTimeProvider dateTimeProvider;
    private final Supplier<RuntimeException> runtimeExceptionSupplier;

    public ProcessorTimeException(DateTimeProvider dateTimeProvider, Supplier<RuntimeException> runtimeExceptionSupplier) {
        this.dateTimeProvider = dateTimeProvider;
        this.runtimeExceptionSupplier = runtimeExceptionSupplier;
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
