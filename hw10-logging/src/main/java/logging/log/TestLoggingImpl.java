package logging.log;

import logging.annotation.Log;

import java.util.Random;

public class TestLoggingImpl implements TestLogging {
    private final Random random = new Random();

    @Log
    @Override
    public void showParams() {
        System.out.println("Метод без параметров!");
    }

    @Log
    @Override
    public void showParams(int first, int second) {
        System.out.println("Значение первого параметра: " + first);
        System.out.println("Значение второго параметра: " + second);
    }

    @Log
    @Override
    public void showParams(int first, int second, int third) {
        System.out.println("Значение первого параметра: " + first);
        System.out.println("Значение второго параметра: " + second);
        System.out.println("Значение третьего параметра: " + third);
    }

    @Log
    @Override
    public int randomNumberInPeriod(int startPeriod, int endPeriod) {
        return random.nextInt((endPeriod - startPeriod) + 1) + startPeriod;
    }

    @Override
    public void notHaveAnnotation() {
        System.out.println("Метод без Аннотации!");
    }
}
