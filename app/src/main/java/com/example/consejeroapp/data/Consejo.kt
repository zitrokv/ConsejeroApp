package com.example.consejeroapp.data

import android.provider.BaseColumns

data class Consejo(var id: Int = -1, var adviceID: Int, var texto: String, var cota: Int = 0, var leida : Boolean = false, var favorita: Boolean = false ) {

    companion object{
        const val TABLE_NAME = "Consejo"
        const val PUTEXTRA_ID = "ADVICE_ID"
        const val SEARCH_ID = "SEARCH_ID"
        const val COLUMNA_ID = "adviceID"
        const val COLUMNA_TEXTO = "texto"
        const val COLUMNA_COTA = "cota"
        const val COLUMNA_LEIDA = "leida"
        const val COLUMNA_FAVORITA = "favorita"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMNA_ID INTEGER," +
                    "$COLUMNA_TEXTO TEXT," +
                    "$COLUMNA_COTA INTEGER," +
                    "$COLUMNA_LEIDA INTEGER," +
                    "$COLUMNA_FAVORITA INTEGER)"

        const val SQL_DROP_TABLE ="DROP TABLE IF EXISTS $TABLE_NAME"

        const val SQL_UPDATE_COTA = "UPDATE $TABLE_NAME SET COTA = COTA + 1"
    }
}