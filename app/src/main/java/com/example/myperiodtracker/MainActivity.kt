package com.example.myperiodtracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myperiodtracker.ui.theme.MyPeriodTrackerTheme

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val editTextSymptoms = findViewById<EditText>(R.id.editTextSymptoms)
        val editTextMood = findViewById<EditText>(R.id.editTextMood)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val entriesList = databaseHelper.getAllEntries()
        val recyclerViewEntries = findViewById<RecyclerView>(R.id.recyclerViewEntries)

        recyclerViewEntries.layoutManager = LinearLayoutManager(this)
        recyclerViewEntries.adapter = EntryAdapter(entriesList)

        buttonSave.setOnClickListener {
            val date = editTextDate.text.toString()
            val symptoms = editTextSymptoms.text.toString()
            val mood = editTextMood.text.toString()

            if (date.isNotEmpty() && symptoms.isNotEmpty() && mood.isNotEmpty()) {
                val result = databaseHelper.addEntry(date, symptoms, mood)
                if (result != -1L) {
                    Toast.makeText(this, "Entry Saved!", Toast.LENGTH_SHORT).show()
                    editTextDate.text.clear()
                    editTextSymptoms.text.clear()
                    editTextMood.text.clear()
                } else {
                    Toast.makeText(this, "Error saving entry", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
