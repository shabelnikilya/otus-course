package logging.log;

import logging.annotation.Log;

public class TestLoggingImpl implements TestLogging {

    @Log
    @Override
    public void calculation() {
        System.out.println("Метод без параметров!");
    }

    @Override
    public void calculation(int first) {
        System.out.println("Метод без аннотации Log");
    }

    @Log
    @Override
    public void calculation(int first, int second) {
        System.out.println("Перегруженный метод с двумя параметрами: " + first + ", " + second);
    }

    @Log
    @Override
    public void calculation(int first, int second, int third) {
        System.out.println("Перегруженный метод с тремя параметрами: " + first + ", " + second + ", " + third);
    }

    @Log
    @Override
    public void calculation(int first, int second, long third) {
        System.out.println("Перегруженный метод с тремя параметрами: " + first + ", " + second + ", " + third);
    }
}
