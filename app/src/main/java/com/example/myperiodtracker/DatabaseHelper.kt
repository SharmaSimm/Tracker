package com.example.myperiodtracker
import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PeriodTracker.db"
        private const val DATABASE_VERSION = 1

        // Table and columns
        const val TABLE_NAME = "CycleEntry"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_SYMPTOMS = "symptoms"
        const val COLUMN_MOOD = "mood"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_ENTRIES_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_DATE TEXT,"
                + "$COLUMN_SYMPTOMS TEXT,"
                + "$COLUMN_MOOD TEXT)")
        db.execSQL(CREATE_ENTRIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Function to add a new entry
    fun addEntry(date: String, symptoms: String, mood: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_SYMPTOMS, symptoms)
        values.put(COLUMN_MOOD, mood)

        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun getAllEntries(): List<CycleEntry> {
        val entries = mutableListOf<CycleEntry>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val symptoms = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SYMPTOMS))
                val mood = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOOD))
                entries.add(CycleEntry(id, date, symptoms, mood))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return entries
    }

    // Function to calculate average symptoms per entry
    fun calculateAverageSymptoms(): Float {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(symptoms) as count FROM $TABLE_NAME", null)

        var totalEntries = 0
        if (cursor.moveToFirst()) {
            totalEntries = cursor.getInt(0)
        }
        cursor.close()

        // Assuming symptoms are stored as a comma-separated string in the database
        val cursorSymptoms = db.rawQuery(
            "SELECT SUM(LENGTH(symptoms) - LENGTH(REPLACE(symptoms, ',', '')) + 1) as symptomCount FROM $TABLE_NAME",
            null
        )

        var totalSymptoms = 0
        if (cursorSymptoms.moveToFirst()) {
            totalSymptoms = cursorSymptoms.getInt(0)
        }
        cursorSymptoms.close()
        db.close()

        return if (totalEntries > 0) {
            totalSymptoms.toFloat() / totalEntries
        } else {
            0.0f // Avoid division by zero
        }
    }

    // Function to get trend information
    @SuppressLint("DefaultLocale")
    fun getTrendInfo(): String {
        // Example: Fetch total number of entries and average mood
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*), AVG(mood) FROM $TABLE_NAME", null)

        var totalEntries = 0
        var averageMood = 0.0
        if (cursor.moveToFirst()) {
            totalEntries = cursor.getInt(0)
            averageMood = cursor.getDouble(1)
        }
        cursor.close()
        db.close()

        return "Total Entries: $totalEntries, Average Mood: ${String.format("%.2f", averageMood)}"
    }

    fun calculateAverageMood(): Float {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT AVG(mood) FROM $TABLE_NAME", null)

        var averageMood = 0.0f
        if (cursor.moveToFirst()) {
            averageMood = cursor.getFloat(0)
        }
        cursor.close()
        db.close()

        return averageMood

    }

    // Function to get the most common symptoms
    fun getMostCommonSymptoms(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT symptoms, COUNT(symptoms) AS freq FROM $TABLE_NAME " +
                    "GROUP BY symptoms ORDER BY freq DESC LIMIT 1",
            null
        )

        var commonSymptoms = ""
        if (cursor.moveToFirst()) {
            commonSymptoms = cursor.getString(0) // Get the most common symptom
        }
        cursor.close()
        db.close()

        return commonSymptoms
    }

}
