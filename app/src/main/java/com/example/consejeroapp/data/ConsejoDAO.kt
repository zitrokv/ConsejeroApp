package com.example.consejeroapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.listadotareasapp.utils.DatabaseManager

class ConsejoDAO(context: Context) {
    private val databaseManager: DatabaseManager = DatabaseManager(context)

    fun insert(consejo: Consejo){
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Consejo.COLUMNA_TEXTO, consejo.texto)
        values.put(Consejo.COLUMNA_COTA, consejo.cota)
        values.put(Consejo.COLUMNA_LEIDA, consejo.leida)

        val newRowId = db.insert(Consejo.TABLE_NAME, null, values)
        consejo.id = newRowId.toInt()
    }

    fun update(consejo: Consejo){
        val db = databaseManager.writableDatabase

        val values= ContentValues()
        values.put(Consejo.COLUMNA_TEXTO, consejo.texto)
        values.put(Consejo.COLUMNA_LEIDA, consejo.leida)

        val updatedRows = db.update(
            Consejo.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ${consejo.id}",
            null
        )

        if (updatedRows == 0)
            upLevel(consejo.id, db)

        //db.close()
    }

    private fun upLevel(id: Int, dbLevel: SQLiteDatabase){
        //se actualizan todos en cada ciclo, no recogemos rowsAffected
        databaseManager.onUpdateCota(dbLevel)
    }


    fun delete(consejo:Consejo) : Int
    {
        var deletedRows = 0
        if (findAll().count() > 1) {
            val db = databaseManager.writableDatabase
            deletedRows =
                db.delete(Consejo.TABLE_NAME, "${BaseColumns._ID} = ${consejo.id}", null)
        }

        //devuelve el numero de filas afectadas, borradas -> si es Cero, entonces mostrar mensaje
        return deletedRows
    }

    fun find(id: Int):Consejo? {

        val db = databaseManager.readableDatabase
        val projection = arrayOf(BaseColumns._ID, Consejo.COLUMNA_TEXTO, Consejo.COLUMNA_COTA, Consejo.COLUMNA_LEIDA)

        val cursor = db.query(
            Consejo.TABLE_NAME,
            projection,
            "${BaseColumns._ID} = $id",
            null,
            null,
            null,
            null
        )

        var consejo: Consejo? = null
        if (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val adviceID = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_TEXTO))
            val cota = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_COTA))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_LEIDA)) == 1
            consejo = Consejo(id, adviceID, name, cota, done)
        }

        cursor.close()
        db.close()
        return consejo
    }
    fun findAll() : List<Consejo> {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Consejo.COLUMNA_TEXTO, Consejo.COLUMNA_COTA, Consejo.COLUMNA_LEIDA)

        val cursor = db.query(
            Consejo.TABLE_NAME,                        // The table to query
            projection,                             // The array of columns to return (pass null to get all)
            null,                            // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            null                             // The sort order
        )

        var tasks = mutableListOf<Consejo>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val adviceID = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_TEXTO))
            val cota = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_COTA))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_LEIDA)) == 1
            val task = Consejo(id, adviceID, name, cota, done)
            tasks.add(task)
        }
        cursor.close()
        db.close()
        return tasks
    }


}