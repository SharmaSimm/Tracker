package com.example.myperiodtracker
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntryAdapter(private val entries: List<CycleEntry>) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.textViewDate)
        val symptomsTextView: TextView = view.findViewById(R.id.textViewSymptoms)
        val moodTextView: TextView = view.findViewById(R.id.textViewMood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.dateTextView.text = entry.date
        holder.symptomsTextView.text = entry.symptoms
        holder.moodTextView.text = entry.mood
    }

    override fun getItemCount() = entries.size
}
