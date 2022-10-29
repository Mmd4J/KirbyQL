package me.gameisntover.database;

import java.util.Locale;

public class DBObject<T> {
    protected T value;
    protected String name;

    protected DBObject(String name, T obj) {
        this.name = name;
        this.value = obj;
    }

    public static <T> DBObject<T> of(String name, T value) {
        return new DBObject<>(name.toUpperCase(Locale.ROOT), value);
    }

    public T get() {
        return value;
    }
}
