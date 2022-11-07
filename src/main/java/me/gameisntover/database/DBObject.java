package me.gameisntover.database;

public class DBObject<T> {
    public T value;
    public String name;

    public boolean primaryKey;

    public boolean doNotUpdate;
    protected DBObject(String name, T obj,boolean pk,boolean doNotUpdate) {
        this.name = name;
        this.value = obj;
        this.primaryKey = pk;
        this.doNotUpdate = doNotUpdate;
    }

    public static <T> DBObject<T> of(String name, T value) {
        return of(name, value,false);
    }
    public static <T> DBObject<T> of(String name, T value,boolean primaryKey) {
        return of(name, value,primaryKey,false);
    }
    public static <T> DBObject<T> of(String name, T value,boolean primaryKey,boolean doNotUpdate) {
        return new DBObject<>(name, value,primaryKey,doNotUpdate);
    }


    public T get() {
        return value;
    }
}
