package me.gameisntover.kirbyql.database.function

import me.gameisntover.kirbyql.database.table.SQLTable
import me.gameisntover.kirbyql.`object`.DBObject
import java.sql.Connection
import java.sql.SQLException

class DeleteFunction(table: SQLTable) : SQLFunction(table) {
    private val tableName: String
    private var sql: String
    private var sqlEnd = ";"

    init {
        this.tableName = table.name
        sql = "DELETE FROM $tableName"
    }

    public fun where(vararg objects: DBObject<*>): DeleteFunction {
        val builder = StringBuilder(" ")
        if (objects.isNotEmpty()) builder.append("WHERE ")
        for (obj in objects) {
            builder.append(obj.name).append("=").append(obj.value.toString())
            if (objects.last() != obj) builder.append(",")
        }
        sqlEnd = builder.toString() + sqlEnd
        return this
    }

    override fun execute() {
        val con: Connection = table.createConnection()
        try {
            val stmt = con.createStatement()
            stmt.executeUpdate(getSQLCode())
            stmt.close()
            con.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            table.database.logger.info("SQL Code: ${getSQLCode()}")
        }
    }

    override fun getSQLCode(): String {
        return sql + sqlEnd
    }

    override fun toString(): String {
        return getSQLCode()
    }
}