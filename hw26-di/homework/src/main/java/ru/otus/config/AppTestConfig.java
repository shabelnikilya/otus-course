package ru.otus.config;


import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.*;

@AppComponentsContainerConfig(order = 2)
public class AppTestConfig {

    @AppComponent(order = 5, name = "testService")
    public TestService testService() {
        return new TestServiceImpl();
    }
}
