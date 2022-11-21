package ru.otus.model;

import ru.otus.listener.homework.Prototype;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Prototype<ObjectForMessage> {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage clone() {
        ObjectForMessage cloneObject = new ObjectForMessage();
        cloneObject.setData(new ArrayList<>(getData()));
        return cloneObject;
    }
}
