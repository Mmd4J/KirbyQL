package me.gameisntover.kirbyql.database.function
import me.gameisntover.kirbyql.database.Database
import me.gameisntover.kirbyql.database.table.SQLTable
import me.gameisntover.kirbyql.`object`.DBObject


class UpdateDataFunction(table: SQLTable, ignore: Boolean) : SQLFunction(table) {
    private var sqlA = ""

    init {
        if (ignore) {
            sqlA += if (table.database.type.equals(Database.Type.SQLite)) "OR IGNORE ${table.name}"
            else "UPDATE ${table.name}"
        }
    }

    override fun execute() {
        val con = table.createConnection()
        val stmt = con.createStatement()
        sqlA += ";"
        stmt.executeUpdate(sqlA)
        stmt.close()
        con.close()
    }

    fun values(vararg objects: DBObject<*>): UpdateDataFunction {
        val builder = StringBuilder(" SET ")
        for (obj in objects) {
            builder.append(obj.name).append("=")
            if (obj.value is String) builder.append("\"").append(obj.value).append("\"")
            else builder.append(obj.value)
            if (obj != objects.last()) {
                builder.append(", ")
            }
        }
        sqlA += builder.toString()
        return this
    }

    fun where(vararg objects: DBObject<*>): UpdateDataFunction {
        val builder = StringBuilder(" WHERE ")
        for (obj in objects) {
            builder.append(obj.name).append("=")
            if (obj.value is String) builder.append("\"").append(obj.value).append("\"")
            else builder.append(obj.value)
            if (obj != objects.last()) {
                builder.append(", ")
            }
        }
        sqlA += builder.toString()
        return this
    }

    override fun getSQLCode(): String {
        return sqlA
    }

}