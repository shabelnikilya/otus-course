package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final Class<AppComponent> componentAnnotation = AppComponent.class;
    private final Class<AppComponentsContainerConfig> containerAnnotation = AppComponentsContainerConfig.class;
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> configClass) {
        processConfig(configClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String packagePath) {
        Reflections reflections = new Reflections(packagePath);
        List<Class<?>> containerClasses = reflections.getTypesAnnotatedWith(containerAnnotation).stream()
                .filter(clazz -> clazz.isAnnotationPresent(containerAnnotation))
                .sorted((clazz1, clazz2) -> Integer.compare(
                                clazz1.getAnnotation(containerAnnotation).order(),
                                clazz2.getAnnotation(containerAnnotation).order()
                        )
                )
                .collect(Collectors.toList());
        if (containerClasses.isEmpty()) {
            throw new ConfigClassNotFoundException("Не найдено ни одного конфигурационного файла для компонентов!");
        }

        containerClasses.forEach(this::processConfig);
    }

    private void processConfig(Class<?>... configClasses) {
        for (Class<?> configClass : configClasses) {
            checkConfigClass(configClass);
            Object componentObject = createComponentObject(configClass);
            List<Method> allMethodsFromClass = Arrays.asList(configClass.getDeclaredMethods());
            List<Method> componentMethod = getComponentMethod(allMethodsFromClass);
            for (Method m : componentMethod) {
                try {
                    Parameter[] parameters = m.getParameters();
                    if (parameters.length == 0) {
                        addComponentToContainer(componentObject, m);
                    } else {
                        int index = 0;
                        Object[] args = new Object[parameters.length];
                        for (Parameter parameter : parameters) {
                            for (Object object : appComponents) {
                                if (object.getClass().equals(parameter.getType())) {
                                    args[index++] = object;
                                    break;
                                }
                                Class<?>[] objectInterfaces = object.getClass().getInterfaces();
                                Class<?>[] oInterfaces;
                                Class<?> parameterType = parameter.getType();
                                if (parameterType.isInterface()) {
                                    oInterfaces = new Class[] { parameterType };
                                } else {
                                    oInterfaces = parameter.getClass().getInterfaces();
                                }
                                validateOnlyOneInterface(objectInterfaces, oInterfaces);
                                if (objectInterfaces[0].equals(oInterfaces[0])) {
                                    args[index++] = object;
                                    break;
                                }
                            }
                        }
                        addComponentToContainer(componentObject, m, args);
                    }
                } catch (Exception e) {
                    throw new ProcessConfigException("Ошибка при конфигурирования контейнера!", e);
                }
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private void addComponentToContainer(Object componentObject, Method method, Object... args) throws IllegalAccessException, InvocationTargetException {
        Object object = method.invoke(componentObject, args);
        appComponents.add(object);
        appComponentsByName.put(method.getAnnotation(componentAnnotation).name(), object);
    }

    private void validateOnlyOneInterface(Class<?>[] objectInterfaces, Class<?>[] oInterfaces) {
        if (oInterfaces.length != 1 && objectInterfaces.length != 1) {
            throw new InterfacesImplementationException(
                    "При передаче управления зависимостями контейнеру по интерфейсу у него должна быть одна реализация!");
        }
    }

    private List<Method> getComponentMethod(List<Method> allMethodsFromClass) {
        Set<String> namesValidate = new HashSet<>();
        return allMethodsFromClass.stream()
                .filter(m -> m.isAnnotationPresent(componentAnnotation))
                .peek(m -> {
                    String nameComponent = m.getAnnotation(componentAnnotation).name();
                    if (!namesValidate.add(nameComponent)) {
                        throw new DuplicateComponentException("В контейнере не может быть двух компонентов с одинаковым именем: " + nameComponent);
                    }
                })
                .sorted((m1, m2) -> Integer.compare(m1.getAnnotation(componentAnnotation).order(), m2.getAnnotation(componentAnnotation).order()))
                .collect(Collectors.toList());
    }

    private Object createComponentObject(Class<?> configClass) {
        Object configObject;
        try {
            configObject = configClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return configObject;
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> results = new ArrayList<>();
        for (Object o : appComponents) {
            if (o.getClass().equals(componentClass) || o.getClass().getInterfaces()[0].equals(componentClass)) {
                results.add((C) o);
            }
        }
        validateResultsFindByComponentClass(componentClass, results);
        return results.get(0);
    }

    private <C> void validateResultsFindByComponentClass(Class<C> componentClass, List<C> results) {
        if (results.isEmpty()) {
            throw new MissingComponentInContainerException("Данный компонент отсутствует в контейнере: " + componentClass.toString());
        }
        if (results.size() != 1) {
            throw new TypeComponentException("Найдено больше одного компонента для " + componentClass.toString());
        }
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        C result = (C) appComponentsByName.get(componentName);
        validateComponentIsPresent(componentName, result);
        return result;
    }

    private <C, T> void validateComponentIsPresent(String componentName, C result) {
        if (result == null) {
            throw new MissingComponentInContainerException("Данный компонент отсутствует в контейнере: " + componentName);
        }
    }
}
