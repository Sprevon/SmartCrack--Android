package net.leonbwchen.smartcrack.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class AccDBHelper(val content: Context, name: String, version: Int) :
    SQLiteOpenHelper(content, name, null, version) {
    private val createAccDB = "create table acc_db (" +
            "username varchar(256) primary key ," +
            "password varchar(20) )"

    private val dropAccDB = "drop table if exists acc_db"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createAccDB)
        Toast.makeText(content, "Create success", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(dropAccDB)
        this.onCreate(db)
    }

}