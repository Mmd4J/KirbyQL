package me.gameisntover.kirbyql.database;

import lombok.SneakyThrows;
import me.gameisntover.kirbyql.database.table.SQLTable;
import me.gameisntover.kirbyql.object.DBObject;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Logger;

import static java.lang.System.out;

public abstract class Database {
    private static final String SQLITE_PREFIX = "jdbc:sqlite:";
    public final HashMap<String, DBObject<String>> objectMap = new HashMap<>();
    private final Type type;
    protected Connection con;
    private String address;
    private String dbName;
    private String user;
    private int port;
    private String password;
    private File file;
    public final Logger log;
    public Database(File file,Logger log) {
        this(log,Type.SQLite);
        this.file = file;
        createConnection();
    }
    private Database(Logger log,Type type){
        this.log = log;
        this.type = type;
    }

    public Database(DatabaseInfo info,Logger log) {
        this(log,info.getDatabaseType());
        ;
        switch (info.getDatabaseType()) {
            case MariaDB:
                this.address = info.getAddress();
                this.port = info.getPort();
                this.dbName = info.getName();
                this.user = info.getUser();
                this.password = info.getPass();
                break;
            case SQLite:
                this.file = info.getFile();
                break;
        }
        createConnection();
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SafeVarargs
    @SneakyThrows
    public final SQLTable createNewTable(String tableName, DBObject<String>... types) {
        createConnection();
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "(");
        for (DBObject<String> obj : types) {
            builder.append(obj.name).append(" ").append(obj.value);
            if (obj.primaryKey) builder.append(" PRIMARY KEY");
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
            con.close();
        } catch (SQLException e) {
            out.println(sql);
            e.printStackTrace();
        }
        return new SQLTable(tableName, this, types);
    }

    public void insertData(String tableName, DBObject<?>... objects) {
        createConnection();
        StringBuilder builder1 = new StringBuilder("INSERT IGNORE INTO ").append(tableName).append("(");
        if (type.equals(Type.SQLite))
            builder1 = new StringBuilder("INSERT OR IGNORE INTO ").append(tableName).append("(");
        StringBuilder builder2 = new StringBuilder(" VALUES(");
        for (DBObject<?> obj : objects) {
            builder1.append(obj.name.toLowerCase(Locale.ROOT));
            if (obj.value instanceof String)
                builder2.append(obj.value);
            else builder2.append(obj.value);
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
            out.println(sql);
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void updateData(String tableName, DBObject<?>... objects) {
        createConnection();
        StringBuilder builder1 = new StringBuilder("UPDATE OR IGNORE ").append(tableName).append(" SET ");
        if (type.equals(Type.MariaDB)) {
            builder1 = new StringBuilder("UPDATE IGNORE ").append(tableName).append(" SET ");
        }
        StringBuilder builder2 = new StringBuilder();
        if (builder2.toString().length() == 0) builder2 = new StringBuilder(" WHERE ");
        for (DBObject<?> obj : objects) {
            if (obj.doNotUpdate) {
                builder2.append(obj.name).append("=").append(obj.value.toString());
                if (objects[objects.length - 1].equals(obj)) {
                    builder2.append(";");
                } else {
                    builder2.append(",");
                }
            } else {
                if (obj.value instanceof String)
                    builder1.append(obj.name).append("=").append(obj.value);
                else builder1.append(obj.name).append("=").append(obj.value.toString());
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
            out.println(sql);
            e.printStackTrace();
        }
    }

    public KirbyResult selectData(String tableName, DBObject<?>... primaryKeys) {
        createConnection();
        StringBuilder b1 = new StringBuilder("SELECT * FROM " + tableName);
        if (primaryKeys.length == 0) {
            b1.append(";");
            try {
                Statement stmt = con.createStatement();
                return new KirbyResult(stmt.executeQuery(b1.toString()));
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        b1.append(" WHERE ");
        for (DBObject<?> obj : primaryKeys) {
            if (obj.value instanceof String) {
                b1.append(obj.name).append("=").append(obj.value).append(" ");
            } else b1.append(obj.name).append("=").append(obj.value).append(" ");
            if (!primaryKeys[primaryKeys.length - 1].equals(obj)) {
                b1.append(",");
            } else {
                b1.append(";");
            }
        }
        KirbyResult sr = null;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(b1.toString());
            sr = new KirbyResult(rs);
        } catch (SQLException e) {
            out.println(b1);
            e.printStackTrace();
        }
        return sr;
    }

    public Type getType() {
        return type;
    }

    public Connection createConnection() {
        try {
            switch (type) {
                case MariaDB:
                    String url = String.format("jdbc:mysql://%s:%d/%s", address, port, dbName);
                    con = DriverManager.getConnection(url, user, password);
                    break;
                case SQLite:
                    con = DriverManager.getConnection(SQLITE_PREFIX + file.getPath());
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public DBObject<String> defineColumn(String name, String type, boolean primaryKey) {
        objectMap.put(name, DBObject.of(name, type, primaryKey, primaryKey));
        return objectMap.get(name);
    }

    public DBObject<String> getColumn(String name) {
        return objectMap.get(name);
    }

    public enum Type {
        MariaDB,
        SQLite,

    }

    public Logger getLogger() {
        return log;
    }
}
