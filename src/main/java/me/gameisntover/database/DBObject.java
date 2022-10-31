package me.gameisntover.database;

import java.util.Locale;

public class DBObject<T> {
    protected T value;
    protected String name;

    protected boolean primaryKey;
    protected DBObject(String name, T obj,boolean pk) {
        this.name = name;
        this.value = obj;
        this.primaryKey = pk;
    }

    public static <T> DBObject<T> of(String name, T value) {
        return of(name.toUpperCase(Locale.ROOT), value,false);
    }
    public static <T> DBObject<T> of(String name, T value,boolean primaryKey) {
        return new DBObject<>(name.toUpperCase(Locale.ROOT), value,primaryKey);
    }

    public T get() {
        return value;
    }
}
