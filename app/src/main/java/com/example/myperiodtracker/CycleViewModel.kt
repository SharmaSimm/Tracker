// CycleViewModel.kt
package com.example.myperiodtracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CycleViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseHelper = DatabaseHelper(application)

    // LiveData for entries
    private val _entries = MutableLiveData<List<CycleEntry>>()
    val entries: LiveData<List<CycleEntry>> get() = _entries

    // LiveData for calculated values
    private val _averageMood = MutableLiveData<Float>()
    val averageMood: LiveData<Float> get() = _averageMood

    private val _averageSymptoms = MutableLiveData<Float>()
    val averageSymptoms: LiveData<Float> get() = _averageSymptoms

    private val _mostCommonSymptoms = MutableLiveData<String>()
    val mostCommonSymptoms: LiveData<String> get() = _mostCommonSymptoms

    init {
        loadEntries()
        calculateStatistics()
    }

    // Function to load entries from the database
    private fun loadEntries() {
        CoroutineScope(Dispatchers.IO).launch {
            val entriesList = databaseHelper.getAllEntries()
            _entries.postValue(entriesList)
        }
    }

    // Function to calculate and update statistics
    private fun calculateStatistics() {
        CoroutineScope(Dispatchers.IO).launch {
            _averageMood.postValue(databaseHelper.calculateAverageMood())
            _averageSymptoms.postValue(databaseHelper.calculateAverageSymptoms())
            _mostCommonSymptoms.postValue(databaseHelper.getMostCommonSymptoms())
        }
    }

    // Function to add a new entry
    fun addEntry(date: String, symptoms: String, mood: String) {
        CoroutineScope(Dispatchers.IO).launch {
            databaseHelper.addEntry(date, symptoms, mood)
            loadEntries() // Refresh entries after adding a new one
            calculateStatistics() // Update statistics
        }
    }
}
