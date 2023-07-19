package me.gameisntover.kirbyql.database.table;

import me.gameisntover.kirbyql.database.Database;
import me.gameisntover.kirbyql.database.KirbyResult;
import me.gameisntover.kirbyql.database.function.DeleteFunction;
import me.gameisntover.kirbyql.database.function.SQLResultFunction;
import me.gameisntover.kirbyql.database.function.SQLFunction;
import me.gameisntover.kirbyql.database.function.SelectFunction;
import lombok.SneakyThrows;
import me.gameisntover.kirbyql.object.DBObject;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Logger;


public class SQLTable {
    private final Database database;
    private final String tableName;
    private final DBObject<?>[] objects;

    public SQLTable(String name, Database database, DBObject<?>... objects) {
        this.tableName = name;
        this.objects = objects;
        this.database = database;
    }

    public void insertData(DBObject<?>... objects) {
        Connection con = database.createConnection();
        StringBuilder builder1 = new StringBuilder("INSERT IGNORE INTO ").append(tableName).append("(");
        if (database.getType().equals(Database.Type.SQLite))
            builder1 = new StringBuilder("INSERT OR IGNORE INTO ").append(tableName).append("(");
        StringBuilder builder2 = new StringBuilder(" VALUES(");
        for (DBObject<?> obj : objects) {
            builder1.append(obj.name.toLowerCase(Locale.ROOT));
            if (obj.value instanceof String) {
                builder2.append("\"").append(obj.value).append("\"");
            } else builder2.append(obj.value);
            if (objects[objects.length - 1].equals(obj)) {
                builder1.append(")");
                builder2.append(");");
            } else {
                builder1.append(",");
                builder2.append(",");
            }
        }
        String sql = builder1.append(builder2).toString();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            database.getLogger().info("SQL Code: " + sql);
        }
    }

    @SneakyThrows
    public void updateData(DBObject<?>... objects) {
        Connection con = database.createConnection();
        StringBuilder builder1 = new StringBuilder("UPDATE OR IGNORE ").append(tableName).append(" SET ");
        if (database.getType().equals(Database.Type.MariaDB)) {
            builder1 = new StringBuilder("UPDATE IGNORE ").append(tableName).append(" SET ");
        }
        StringBuilder builder2 = new StringBuilder();
        if (builder2.toString().length() == 0) builder2 = new StringBuilder(" WHERE ");
        for (DBObject<?> obj : objects) {
            if (obj.doNotUpdate) {
                if (obj.value instanceof String)
                    builder2.append(obj.name).append("=").append("\"").append(obj.value).append("\"");
                else builder2.append(obj.name).append("=").append(obj.value.toString());
                if (objects[objects.length - 1].equals(obj)) {
                    builder2.append(";");
                } else {
                    builder2.append(",");
                }
            } else {
                if (obj.value instanceof String)
                    builder1.append(obj.name).append("=").append("\"").append(obj.value).append("\"");
                else builder1.append(obj.name).append("=").append(obj.value);
                if (objects[objects.length - 1].equals(obj)) {
                    builder2.append(";");
                } else {
                    builder1.append(",");
                }
            }
        }
        String sql = builder1.append(builder2).toString();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().info("SQL Code: " + sql);
        }
    }


    public KirbyResult selectData(DBObject<?>... primaryKeys) {
        SelectFunction func = new SelectFunction(this).where(primaryKeys);
        return func.executeQuery();
    }

    @SafeVarargs
    public final void deleteData(DBObject<Object>... objects) {
        DeleteFunction func = new DeleteFunction(this).where(objects);
        func.execute();
    }

    public String getName() {
        return tableName;
    }

    @NotNull
    public Connection createConnection() {
        return database.createConnection();
    }

    @SneakyThrows
    public SQLFunction requestFunction(Class<? extends SQLFunction> type) {
        return (SQLFunction) type.getConstructors()[0].newInstance(this);
    }

    @SneakyThrows
    public SQLResultFunction requestDataFunction(Class<? extends SQLResultFunction> type) {
        return (SQLResultFunction) type.getConstructors()[0].newInstance(this);
    }

    public Database getDatabase() {
        return database;
    }

    public Logger getLogger(){
        return database.log;
    }
}
