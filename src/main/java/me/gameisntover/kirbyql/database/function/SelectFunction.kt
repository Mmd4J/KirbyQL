package me.gameisntover.kirbyql.database.function

import me.gameisntover.kirbyql.database.KirbyResult
import me.gameisntover.kirbyql.database.table.SQLTable
import me.gameisntover.kirbyql.`object`.DBObject
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class SelectFunction(table: SQLTable) : SQLResultFunction(table) {
    private var sqlA = "SELECT * FROM ${table.name}"
    private var rsType = ResultSet.TYPE_SCROLL_SENSITIVE
    private var rsCon = ResultSet.CONCUR_READ_ONLY

    override fun executeQuery(): KirbyResult {
        val con: Connection = table.createConnection()
        var sr: KirbyResult? = null
        try {
            val stmt = con.createStatement(rsType,rsCon)
            val rs = stmt.executeQuery(getSQLCode())
            rs.next();
            sr = KirbyResult(rs)
        } catch (e: SQLException) {
            e.printStackTrace()
            table.database.logger.info("SQL Code: ${getSQLCode()}")
        }
        return checkNotNull(sr)
    }

    fun where(vararg primaryKeys: DBObject<*>): SelectFunction {
        val b1 = StringBuilder(sqlA)
        b1.append(" WHERE ")
        for (obj in primaryKeys) {
            if (obj.value is String) {
                b1.append(obj.name).append("=").append("\"").append(obj.value).append("\"").append(" ")
            } else b1.append(obj.name).append("=").append(obj.value).append(" ")
            if (primaryKeys[primaryKeys.size - 1] != obj) {
                b1.append(",")
            } else {
                b1.append(" ")
            }
        }
        sqlA = b1.toString()
        return this
    }

    fun limit(limit: Int): SelectFunction {
        sqlA += " LIMIT $limit"
        return this;
    }

    fun order(row: String, desc: Boolean = false): SelectFunction {
        val b1 = StringBuilder(sqlA)
        b1.append(" ORDER BY $row")
        if (desc)
            b1.append(" DESC")
        sqlA = b1.toString()
        return this
    }

    override fun getSQLCode(): String {
        return sqlA
    }

}