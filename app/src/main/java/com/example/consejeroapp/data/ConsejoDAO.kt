package com.example.consejeroapp.data

import android.content.ContentValues
import android.content.Context
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
            upLevel()

        db.close()
    }

    private fun upLevel(){
        val AllList = findAll()
        val dbLevel = databaseManager.writableDatabase
        var values = ContentValues()
        AllList.forEach{
            values.clear()
            //values.put(Consejo.COLUMNA_TEXTO, it.texto)
            values.put(Consejo.COLUMNA_COTA, it.cota++)
            //se actualizan todos en cada ciclo, no recogemos rowsAffected
            val updatedRows = dbLevel.update(
                Consejo.TABLE_NAME,
                values,
                "${BaseColumns._ID} = ${it.id}",
                null
            )
        }
        dbLevel.close()

    }


    fun delete(tarea:Consejo)
    {
        val db = databaseManager.writableDatabase
        val deletedRows = db.delete(Consejo.TABLE_NAME, "${BaseColumns._ID} = ${tarea.id}",null)
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

        var tarea: Consejo? = null
        if (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_TEXTO))
            val cota = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_COTA))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_LEIDA)) == 1
            tarea = Consejo(id, name, cota, done)
        }

        cursor.close()
        db.close()
        return tarea
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
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_TEXTO))
            val cota = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_COTA))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Consejo.COLUMNA_LEIDA)) == 1
            val task = Consejo(id, name, cota, done)
            tasks.add(task)
        }
        cursor.close()
        db.close()
        return tasks
    }


}