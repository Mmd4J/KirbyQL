package me.gameisntover.kirbyql.database.function

import me.gameisntover.kirbyql.database.KirbyResult
import me.gameisntover.kirbyql.database.table.SQLTable
import me.gameisntover.kirbyql.exception.SQLFunctionException

abstract class SQLResultFunction(table: SQLTable) : SQLFunction(table) {
    abstract fun executeQuery(): KirbyResult

    override fun execute() {
        throw SQLFunctionException("You can't execute a SQLResultFunction")
    }
}