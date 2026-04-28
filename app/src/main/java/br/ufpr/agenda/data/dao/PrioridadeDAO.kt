package br.ufpr.agenda.data.dao

import android.content.Context
import android.database.Cursor
import br.ufpr.agenda.data.DBHelper
import br.ufpr.agenda.data.model.PrioridadeModel
class PrioridadeDAO (private val context: Context) {

    private val dbHelper = DBHelper(context)

    fun findById(id: Int): PrioridadeModel? {
        if (id == 0) {
            return null
        }
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DBHelper.PRIORIDADE_TABLE,
            null,
            "id=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var prioridade: PrioridadeModel? = null

        if (cursor.moveToFirst()) {
            prioridade = PrioridadeModel(
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                peso=cursor.getInt(cursor.getColumnIndexOrThrow("peso")),
                nome=cursor.getString(cursor.getColumnIndexOrThrow("nome")),
            )
        }
        cursor.close()
        db.close()

        return prioridade
    }

    fun findAll(): List<PrioridadeModel> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DBHelper.PRIORIDADE_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val prioridadeList = mutableListOf<PrioridadeModel>()
        while (cursor.moveToNext()) {
            val prioridade = PrioridadeModel(
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nome=cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                peso=cursor.getInt(cursor.getColumnIndexOrThrow("peso"))
            )
            prioridadeList.add(prioridade)
        }
        cursor.close()
        db.close()
        return prioridadeList
    }
}