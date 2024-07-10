package com.example.listadotareasapp.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.consejeroapp.data.Consejo

class DatabaseManager(context:Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Consejos.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Consejo.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(Consejo.SQL_DROP_TABLE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun onUpdateCota(db:SQLiteDatabase){
        db.execSQL(Consejo.SQL_UPDATE_COTA)
    }
}