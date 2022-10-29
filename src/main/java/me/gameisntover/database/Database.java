package me.gameisntover.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public abstract class Database {
    protected Connection con;
    public Database(File file){
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Database(String address,String dbName,int port ,String user, String password){
        try {
            con = DriverManager.getConnection("jdbc:mysql://"+ address + ":"+ port + "/"+ dbName,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SafeVarargs
    public final void createNewTable(String tableName, DBObject<String>... types){
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS" + tableName + "(");
        for (DBObject<String> obj : types) {
            builder.append(obj.name).append(" ").append(obj.value);
            if (types[types.length-1].equals(types[Arrays.stream(types).toList().indexOf(obj)]))
                builder.append(")");
        }
        String sql = builder.toString();
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public enum Type{
        MySQL,
        SQLite
    }
}
