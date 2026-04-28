package br.ufpr.agenda.data.dao

import android.content.Context
import android.database.Cursor
import br.ufpr.agenda.data.DBHelper
import br.ufpr.agenda.data.model.StatusModel

class StatusDAO (private val context: Context) {

    private val dbHelper = DBHelper(context)

    fun findById(id: Int): StatusModel? {
        if (id == 0) {
            return null
        }
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DBHelper.STATUS_TABLE,
            null,
            "id=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var status: StatusModel? = null

        if (cursor.moveToFirst()) {
            status = StatusModel(
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                peso=cursor.getInt(cursor.getColumnIndexOrThrow("peso")),
                nome=cursor.getString(cursor.getColumnIndexOrThrow("nome")),
            )
        }
        cursor.close()
        db.close()

        return status
    }


    fun findAll(): List<StatusModel> {
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

        val statusList = mutableListOf<StatusModel>()
        while (cursor.moveToNext()) {
            val status = StatusModel(
                id=cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nome=cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                peso=cursor.getInt(cursor.getColumnIndexOrThrow("peso"))
            )
            statusList.add(status)
        }
        cursor.close()
        db.close()
        return statusList
    }
}