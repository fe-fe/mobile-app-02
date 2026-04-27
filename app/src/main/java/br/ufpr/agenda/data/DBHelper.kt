package br.ufpr.agenda.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "Agenda.db"
        const val DATABASE_VERSION = 2
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

        // criar tabela de prioridade
        db.execSQL("""
            CREATE TABLE prioridade (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT
            )
        """.trimIndent())
        // inserir entradas de prioridade
        db.execSQL("INSERT INTO prioridade VALUES ('baixa')")
        db.execSQL("INSERT INTO prioridade VALUES ('média')")
        db.execSQL("INSERT INTO prioridade VALUES ('alta')")

        // criar tabela de tarefas
        val createTarefasTable = """
            CREATE TABLE tarefas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT,
                descricao TEXT,
                id_prioridade INTEGER,
                id_status INTEGER,
                FOREIGN KEY (fk_status_id) REFERENCES status(id),
                FOREIGN KEY (fk_prioridade_id) REFERENCES prioridade(id)
            )
        """.trimIndent()
        db.execSQL(createTarefasTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS status")
        db.execSQL("DROP TABLE IF EXISTS prioridade")
        db.execSQL("DROP TABLE IF EXISTS tarefas")
    }

}
