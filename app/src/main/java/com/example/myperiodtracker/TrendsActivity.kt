package com.example.myperiodtracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TrendsActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trends)

        databaseHelper = DatabaseHelper(this)

        val averageMoodTextView = findViewById<TextView>(R.id.textViewAverageMood)
        val commonSymptomsTextView = findViewById<TextView>(R.id.textViewCommonSymptoms)
        val averageSymptomsTextView = findViewById<TextView>(R.id.textViewAverageSymptoms)
        val trendInfoTextView = findViewById<TextView>(R.id.textViewTrendInfo)
        val backButton = findViewById<Button>(R.id.buttonBack)

        // Fetch data from the database
        val averageMood = databaseHelper.calculateAverageMood()
        val commonSymptoms = databaseHelper.getMostCommonSymptoms()
        val averageSymptoms = databaseHelper.calculateAverageSymptoms() // Example function to implement
        val trendInfo = databaseHelper.getTrendInfo() // Example function to implement

        // Update TextViews with data
        averageMoodTextView.text = "Average Mood: $averageMood"
        commonSymptomsTextView.text = "Most Common Symptoms: $commonSymptoms"
        averageSymptomsTextView.text = "Average Symptoms per Entry: $averageSymptoms"
        trendInfoTextView.text = "Trend Information: $trendInfo"

        backButton.setOnClickListener {
            finish() // Closes the TrendsActivity and returns to the previous activity
        }
    }
}
