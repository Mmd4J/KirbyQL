package me.gameisntover.database;

import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Database {
    protected Connection con;

    public Database(File file) {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Database(String address, String dbName, int port, String user, String password) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + dbName, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createNewTable(String tableName, DBObject<String>... types) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS" + tableName + "(");
        for (DBObject<String> obj : types) {
            builder.append(obj.name).append(" ").append(obj.value);
            if (obj.primaryKey) builder.append("PRIMARY KEY ");
            if (types[types.length - 1].equals(obj))
                builder.append(")");
            else builder.append(",");
        }
        builder.append(";");
        String sql = builder.toString();
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertData(String tableName, DBObject<?>... objects) {
        StringBuilder builder1 = new StringBuilder("INSERT OR IGNORE INTO ").append(tableName).append("(");
        StringBuilder builder2 = new StringBuilder(" VALUES(");
        for (DBObject<?> obj : objects) {
            builder1.append(obj.name);
            builder2.append(obj.value.toString());
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateData(String tableName, DBObject<?>... objects) {
        StringBuilder builder1 = new StringBuilder("UPDATE OR IGNORE ").append(tableName).append("SET ");
        StringBuilder builder2 = new StringBuilder(" WHERE ");
        for (DBObject<?> obj : objects) {
            builder1.append(obj.name);
            builder2.append(obj.value.toString());
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SQLResult selectData(String tableName, DBObject<?>... primaryKeys) {
        StringBuilder b1 = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
        for (DBObject<?> obj : primaryKeys) {
        b1.append(obj.name).append("=").append(obj.value).append(" ");
        if (!primaryKeys[primaryKeys.length-1].equals(obj)){
            b1.append(",");
        }else {
            b1.append(";");
        }
        }
        SQLResult sr;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(b1.toString());
            sr = new SQLResult(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sr;
    }

    public enum Type {
        MySQL,
        SQLite
    }
}
