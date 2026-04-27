package br.ufpr.agenda.data.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "Agenda.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // criar tabela de status
        db.execSQL("""
            CREATE TABLE status (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT
            )
        """.trimIndent())
        // criar entradas de status
        db.execSQL("INSERT INTO status VALUES ('pendente')")
        db.execSQL("INSERT INTO status VALUES ('andamento')")
        db.execSQL("INSERT INTO status VALUES ('concluído')")

        val createTarefasTable = """
            CREATE TABLE tarefas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT,
                descricao TEXT
            )
        """.trimIndent()
        db.execSQL(createTarefasTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS status")
        db.execSQL("DROP TABLE IF EXISTS tarefas")
    }

}
