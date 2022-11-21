package ru.otus.model;

import ru.otus.jdbc.mapper.reflection.entity.annotation.Id;
import ru.otus.jdbc.mapper.reflection.entity.annotation.InitEntity;

public class Client {
    @Id
    private Long id;
    private String name;

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    @InitEntity
    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(Client.class.getSimpleName().toLowerCase());
    }
}
