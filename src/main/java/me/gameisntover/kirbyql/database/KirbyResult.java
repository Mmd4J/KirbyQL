package me.gameisntover.kirbyql.database;


import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class KirbyResult {
    private final ResultSet rs;
    private Statement stmt;

    public KirbyResult(ResultSet rs) {
        this.rs = rs;
        try {
            if (rs != null && !rs.isClosed())
                stmt = rs.getStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public boolean isNull() {
        return rs == null || stmt.getConnection() == null || stmt == null || rs.isClosed();
    }

    public ResultSet getResultSet() {
        return rs;
    }

    public Statement getStatement() {
        return stmt;
    }

    @SneakyThrows
    public void close() {
        stmt.getConnection().close();
        if (rs != null && !rs.isClosed()) getResultSet().close();
        if (stmt != null) getStatement().close();
    }
}
