package com.example.consejeroapp.data

import android.provider.BaseColumns

data class Consejo(var id: Int = -1, var texto: String, var leida : Boolean = false, var favorita: Boolean = false ) {

    companion object{
        const val TABLE_NAME = "Consejo"
        const val COLUMNA_TEXTO = "texto"
        const val COLUMNA_LEIDA = "leida"
        const val COLUMNA_FAVORITA = "favorita"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMNA_TEXTO TEXT," +
                    "$COLUMNA_LEIDA INTEGER," +
                    "$COLUMNA_FAVORITA INTEGER)"

        const val SQL_DROP_TABLE ="DROP TABLE IF EXISTS $TABLE_NAME"
    }
}