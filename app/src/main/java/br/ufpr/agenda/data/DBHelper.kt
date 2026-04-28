package br.ufpr.agenda.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "Agenda.db"
        const val DATABASE_VERSION = 2
        const val PRIORIDADE_TABLE = "prioridade"
        const val TAREFAS_TABLE = "tarefas"
        const val STATUS_TABLE = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // criar tabela de status
        db.execSQL("""
            CREATE TABLE $STATUS_TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                peso INTEGER UNIQUE NOT NULL,
                nome TEXT NOT NULL
            )
        """.trimIndent())
        // criar entradas de status
        db.execSQL("INSERT INTO $STATUS_TABLE (peso, nome) VALUES (3, 'pendente')")
        db.execSQL("INSERT INTO $STATUS_TABLE (peso, nome) VALUES (2, 'andamento')")
        db.execSQL("INSERT INTO $STATUS_TABLE (peso, nome) VALUES (1, 'concluído')")

        // criar tabela de prioridade
        db.execSQL("""
            CREATE TABLE $PRIORIDADE_TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                peso INTEGER UNIQUE NOT NULL,
                nome TEXT NOT NULL
            )
        """.trimIndent())
        // inserir entradas de prioridade
        db.execSQL("INSERT INTO $PRIORIDADE_TABLE (peso, nome) VALUES (1, 'baixa')")
        db.execSQL("INSERT INTO $PRIORIDADE_TABLE (peso, nome) VALUES (2, 'média')")
        db.execSQL("INSERT INTO $PRIORIDADE_TABLE (peso, nome) VALUES (3, 'alta')")

        // criar tabela de tarefas
        val createTarefasTable = """
            CREATE TABLE $TAREFAS_TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT,
                descricao TEXT,
                id_prioridade INTEGER DEFAULT 1,
                id_status INTEGER DEFAULT 1,
                FOREIGN KEY (id_status) REFERENCES status(id),
                FOREIGN KEY (id_prioridade) REFERENCES prioridade(id)
            )
        """.trimIndent()
        db.execSQL(createTarefasTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TAREFAS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $STATUS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $PRIORIDADE_TABLE")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

}
