package me.gameisntover.database;

import java.io.File;

public class SQLiteDB extends Database{
    public SQLiteDB(File file) {
        super(file);
    }

    @Override
    public Type getType() {
        return Type.SQLite;
    }
}
