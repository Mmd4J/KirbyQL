package me.gameisntover.database;

import java.io.File;

public class DatabaseInfo {
    private final Database.Type databaseType;
    private String name;
    private String user;
    private String address;
    private String pass;
    private int port;

    private File file;
    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getUser() {
        return user;
    }

    public Database.Type getDatabaseType() {
        return databaseType;
    }

    public File getFile() {
        return file;
    }

    public DatabaseInfo(File file){
        databaseType = Database.Type.SQLite;
        this.file = file;
    }

    public DatabaseInfo(String name,String address,int port,String user,String pass){
        this.name = name;
        this.address = address;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.databaseType = Database.Type.MySQL;
    }
}
