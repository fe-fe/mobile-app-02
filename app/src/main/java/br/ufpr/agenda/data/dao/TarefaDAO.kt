package br.ufpr.agenda.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.ufpr.agenda.data.DBHelper
import br.ufpr.agenda.data.model.TarefaModel

class TarefaDAO (private val context: Context) {

    private val dbHelper = DBHelper(context)

    fun create(tarefa: TarefaModel): Long {

        if (tarefa.titulo.isBlank()) {
            return 0
        }

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
        if (tarefa.id == 0) {
            return 0
        }
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

    fun findById(id: Int): TarefaModel? {
        if (id == 0) {
            return null
        }
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBHelper.TAREFAS_TABLE,
            null,
            "id=?",
            arrayOf(id.toString()),
            null, null, null
        )

        var tarefa: TarefaModel? = null
        if (cursor.moveToFirst()) {
            tarefa = TarefaModel(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                id_status = cursor.getInt(cursor.getColumnIndexOrThrow("id_status")),
                id_prioridade = cursor.getInt(cursor.getColumnIndexOrThrow("id_prioridade"))
            )
        }
        cursor.close()
        db.close()
        return tarefa
    }

    fun find(statusId: Int? = null, prioridadeId: Int? = null, titulo: String? = null): List<TarefaModel> {
        val db = dbHelper.readableDatabase
        val conditions = mutableListOf<String>()
        val args = mutableListOf<String>()
        var where: String?

        if (!titulo.isNullOrBlank()) {
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
            where = conditions.joinToString(" AND ")
        } else {
            where = null
        }

        val cursor: Cursor = db.query(
            DBHelper.TAREFAS_TABLE,
            null,
            where,
            args.toTypedArray(),
            null,
            null,
            null
        )

        val tarefaList = mutableListOf<TarefaModel>()
        while (cursor.moveToNext()) {
            val tarefa = TarefaModel(
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                titulo=cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descricao=cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                id_status=cursor.getInt(cursor.getColumnIndexOrThrow("id_status")),
                id_prioridade=cursor.getInt(cursor.getColumnIndexOrThrow("id_prioridade"))
            )
            tarefaList.add(tarefa)
        }
        cursor.close()
        db.close()
        return tarefaList
    }

    fun delete(id: Int): Int {
        if (id == 0) {
            return 0
        }
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
