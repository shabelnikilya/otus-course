package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.handler.Handler;
import ru.otus.listener.Listener;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.ProcessorChangePlaceField;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {
        Message message = new Message.Builder(1L)
                .field10("10")
                .field11("11")
                .field12("12")
                .build();

        Processor processor = new LoggerProcessor(new ProcessorChangePlaceField());
        List<Processor> processorList = List.of(processor);
        Listener listenerPrintConsole = new ListenerPrinterConsole();

        Handler handler = new ComplexProcessor(processorList, c -> {});
        handler.addListener(listenerPrintConsole);
        handler.handle(message);


        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
    }
}
