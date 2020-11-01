package com.kdimitrov.edentist.model;

public class Result<T> {
    boolean persisted;
    T entity;

    public Result(T entity, boolean persisted) {
        this.entity = entity;
        this.persisted = persisted;
    }

    public T get() {
        return entity;
    }

    public boolean successful() {
        return persisted;
    }
}
