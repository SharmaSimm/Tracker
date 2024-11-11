// TrendsActivity.kt
package com.example.myperiodtracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class TrendsActivity : AppCompatActivity() {
    private val cycleViewModel: CycleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trends)

        val averageMoodTextView = findViewById<TextView>(R.id.textViewAverageMood)
        val commonSymptomsTextView = findViewById<TextView>(R.id.textViewCommonSymptoms)
        val averageSymptomsTextView = findViewById<TextView>(R.id.textViewAverageSymptoms)
        val trendInfoTextView = findViewById<TextView>(R.id.textViewTrendInfo)
        val backButton = findViewById<Button>(R.id.buttonBack)

        // Observing and displaying the data from ViewModel
        cycleViewModel.averageMood.observe(this, Observer { averageMood ->
            averageMoodTextView.text = "Average Mood: $averageMood"
        })

        cycleViewModel.mostCommonSymptoms.observe(this, Observer { commonSymptoms ->
            commonSymptomsTextView.text = "Most Common Symptoms: $commonSymptoms"
        })

        cycleViewModel.averageSymptoms.observe(this, Observer { averageSymptoms ->
            averageSymptomsTextView.text = "Average Symptoms per Entry: $averageSymptoms"
        })

        // Assuming getTrendInfo is handled by calculating both the average mood and the most common symptoms
        cycleViewModel.entries.observe(this, Observer { entries ->
            trendInfoTextView.text = "Total Entries: ${entries.size}"
        })

        backButton.setOnClickListener {
            finish()
        }
    }
}
