package net.leonbwchen.smartcrack.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class ImgDBHelper(val content: Context, name: String = "smart_crack.db", version: Int) :
    SQLiteOpenHelper(content, name, null, version) {

    private val createDBOri = "create table if not exists pic_info (" +
            "pic_num integer primary key autoincrement," +
            "pic_id varchar(20) , " +
            "pic_src varchar," +
            "pic_time datetime," +
            "pic_operator varchar(10)," +
            "pic_address varchar(256))"

    private val createDBAft = "create table if not exists pic_info_after (" +
            "pic_num integer primary key autoincrement," +
            "pic_src varchar)"

    private val dropDBOri = "drop table if exists pic_info"
    private val dropDBAft = "drop table if exists pic_info_after"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createDBOri)
        db?.execSQL(createDBAft)
        Toast.makeText(content, "Create success", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(dropDBOri)
        db?.execSQL(dropDBAft)
        this.onCreate(db)
    }

}