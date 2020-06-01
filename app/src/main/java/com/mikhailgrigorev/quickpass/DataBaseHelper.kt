package com.mikhailgrigorev.quickpass

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    val TABLE_USERS = "users"
    val KEY_ID = "_id"
    val KEY_NAME = "name"
    val KEY_PASS = "pass"
    val KEY_IMAGE = "avatar"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + TABLE_USERS + "(" + KEY_ID
                    + " integer primary key," + KEY_NAME + " text," + KEY_PASS + " text," + KEY_IMAGE +" text"+ ")"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("drop table if exists $TABLE_USERS")
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "UserDatabase"
    }
}