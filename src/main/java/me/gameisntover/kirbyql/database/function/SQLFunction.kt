package me.gameisntover.kirbyql.database.function

import me.gameisntover.kirbyql.database.table.SQLTable

abstract class SQLFunction(val table: SQLTable) {
    abstract fun execute()
    abstract fun getSQLCode(): String
}