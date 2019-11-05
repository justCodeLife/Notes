package com.example.notes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager {
    val DBName = "Notes"
    val DBTable = "Notes"
    val colID = "ID"
    val colTitle = "Title"
    val colDescription = "Description"
    val DBVersion = 1
    val sqlCreateTable =
        "CREATE TABLE IF NOT EXISTS " + DBTable + " (" + colID + " INTEGER PRIMARY KEY , " + colTitle + " TEXT , " + colDescription + " TEXT);"

    var sqlDB: SQLiteDatabase? = null

    constructor(context: Context) {
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes : SQLiteOpenHelper {
        var context: Context? = null

        constructor(context: Context) : super(context, DBName, null, DBVersion) {
            this.context = context
        }

        override fun onCreate(p0: SQLiteDatabase?) {

            p0?.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "database created", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

            p0?.execSQL("DROP TABLE IF EXISTS $DBTable")
        }
    }


    fun Insert(values: ContentValues): Long? {
        val ID = sqlDB?.insert(DBTable, "", values)
        return ID
    }

    fun query(projection: Array<String>?, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = DBTable
        val cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB!!.delete(DBTable, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB!!.update(DBTable, values, selection, selectionArgs)
        return count
    }

}