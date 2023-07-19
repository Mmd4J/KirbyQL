package me.gameisntover.kirbyql.database.function

import me.gameisntover.kirbyql.database.Database
import me.gameisntover.kirbyql.database.table.SQLTable
import me.gameisntover.kirbyql.`object`.DBObject

class InsertDataFunction(table: SQLTable, ignore: Boolean) : SQLFunction(table) {
    private var sqlA = "INSERT "

    init {
        if (ignore) {
            sqlA += if (table.database.type.equals(Database.Type.SQLite)) "OR IGNORE INTO ${table.name}("
            else "IGNORE INTO ${table.name}("
        }
    }

    override fun execute() {
        val con = table.createConnection()
        val stmt = con.createStatement();
        stmt.executeUpdate(sqlA)
        stmt.close()
        con.close()
    }

    fun values(vararg objects: DBObject<*>): InsertDataFunction {
        val builder = StringBuilder(sqlA)
        val builder2 = StringBuilder(") VALUES(")
        for (obj in objects) {
            builder.append(obj.name)
            if (obj.value is String) builder2.append("\"").append(obj.value).append("\"")
            else builder2.append(obj.value)

            if (obj == objects.last()) {
                builder2.append(")")
            }
        }
        sqlA = builder.toString() + builder2.toString()
        return this
    }

    override fun getSQLCode(): String {
        return sqlA
    }
}