package ru.otus.services;

public class TestServiceImpl implements TestService {

    @Override
    public void showTest() {
        System.out.println("В работе тестовый сервис, компонент создан контейнером!");
    }
}
