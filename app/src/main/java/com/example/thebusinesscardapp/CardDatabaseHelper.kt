package com.example.thebusinesscardapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class BusinessCard(
    val id: Int = 0,
    val name: String,
    val title: String,
    val phone: String,
    val email: String,
    val location: String
)

class BusinessCardDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "businesscard.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "business_card"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TITLE = "title"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_LOCATION = "location"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_TITLE TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_LOCATION TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertBusinessCard(card: BusinessCard): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, card.name)
            put(COLUMN_TITLE, card.title)
            put(COLUMN_PHONE, card.phone)
            put(COLUMN_EMAIL, card.email)
            put(COLUMN_LOCATION, card.location)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllBusinessCards(): List<BusinessCard> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val businessCards = mutableListOf<BusinessCard>()
        with(cursor) {
            while (moveToNext()) {
                val card = BusinessCard(
                    id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    name = getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    title = getString(getColumnIndexOrThrow(COLUMN_TITLE)),
                    phone = getString(getColumnIndexOrThrow(COLUMN_PHONE)),
                    email = getString(getColumnIndexOrThrow(COLUMN_EMAIL)),
                    location = getString(getColumnIndexOrThrow(COLUMN_LOCATION))
                )
                businessCards.add(card)
            }
        }
        cursor.close()
        return businessCards
    }

    fun updateBusinessCard(card: BusinessCard): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, card.name)
            put(COLUMN_TITLE, card.title)
            put(COLUMN_PHONE, card.phone)
            put(COLUMN_EMAIL, card.email)
            put(COLUMN_LOCATION, card.location)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(card.id.toString()))
    }

    fun deleteBusinessCard(card: BusinessCard): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(card.id.toString()))
    }
}