package br.ufpr.agenda.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.ufpr.agenda.data.DBHelper
import br.ufpr.agenda.data.model.StatusModel
import br.ufpr.agenda.data.model.TarefaModel

class TarefaDAO (private val context: Context) {

    private val dbHelper = DBHelper(context)

    fun create(tarefa: TarefaModel): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", tarefa.titulo)
            put("descricao", tarefa.descricao)
            put("id_prioridade", tarefa.id_prioridade)
            put("id_status", tarefa.id_status)
        }
        val id = db.insert(DBHelper.TAREFAS_TABLE, null, values)
        db.close()
        return id
    }

    fun update(tarefa: TarefaModel): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", tarefa.titulo)
            put("descricao", tarefa.descricao)
            put("id_prioridade", tarefa.id_prioridade)
            put("id_status", tarefa.id_status)
        }
        val rowsAffected = db.update(
            DBHelper.TAREFAS_TABLE,
            values,
            "id=?",
            arrayOf(tarefa.id.toString())
        )
        db.close()
        return rowsAffected
    }

    fun findByStatus(statusID: Int): List<TarefaModel> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DBHelper.STATUS_TABLE,
            null,
            "id_status=?",
            arrayOf(statusID.toString()),
            null,
            null,
            null
        )

        val tarefaList = mutableListOf<TarefaModel>()
        while (cursor.moveToNext()) {
            val status = TarefaModel(
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                titulo=cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descricao=cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                id_status=cursor.getInt(cursor.getColumnIndexOrThrow("id_status")),
                id_prioridade=cursor.getInt(cursor.getColumnIndexOrThrow("id_prioridade"))
            )
            tarefaList.add(status)
        }
        cursor.close()
        db.close()
        return tarefaList
    }

    fun find(statusId: Int? = null, prioridadeId: Int? = null, titulo: String? = null): List<TarefaModel> {
        val db = dbHelper.readableDatabase
        val conditions = mutableListOf<String>()
        var args = mutableListOf<String>()
        var where = ""


        if (titulo != null) {
            conditions.add("titulo LIKE ?")
            args.add("%$titulo%")
        }

        if (statusId != null) {
            conditions.add("id_status = ?")
            args.add(statusId.toString())
        }

        if (prioridadeId != null) {
            conditions.add("id_prioridade = ?")
            args.add(prioridadeId.toString())
        }

        if (!conditions.isEmpty()) {
            var where = conditions.joinToString(" AND ")
        } else {
            var where = null
        }

        val cursor: Cursor = db.query(
            DBHelper.STATUS_TABLE,
            null,
            where,
            args.toTypedArray(),
            null,
            null,
            null
        )

        val tarefaList = mutableListOf<TarefaModel>()
        while (cursor.moveToNext()) {
            val status = TarefaModel(
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                titulo=cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descricao=cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                id_status=cursor.getInt(cursor.getColumnIndexOrThrow("id_status")),
                id_prioridade=cursor.getInt(cursor.getColumnIndexOrThrow("id_prioridade"))
            )
            tarefaList.add(status)
        }
        cursor.close()
        db.close()
        return tarefaList
    }

    fun findAll(): List<TarefaModel> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DBHelper.STATUS_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val tarefaList = mutableListOf<TarefaModel>()
        while (cursor.moveToNext()) {
            val status = TarefaModel(
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                titulo=cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descricao=cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                id_status=cursor.getInt(cursor.getColumnIndexOrThrow("id_status")),
                id_prioridade=cursor.getInt(cursor.getColumnIndexOrThrow("id_prioridade"))
            )
            tarefaList.add(status)
        }
        cursor.close()
        db.close()
        return tarefaList
    }

    fun delete(id: Int): Int {
        val db = dbHelper.writableDatabase
        val rowsDeleted = db.delete(
            DBHelper.TAREFAS_TABLE,
            "id=?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsDeleted
    }
}