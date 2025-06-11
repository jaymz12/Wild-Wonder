package com.example.wild_wonders.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wild_wonders.R

class MyAdapter(private val context: Context, private val dataList: ArrayList<AddDetailsData>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.display, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.nameTextView.text = data.name
        holder.locationTextView.text = data.observationLocation
        holder.speciesTextView.text = data.species
        holder.descriptionTextView.text = data.description
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.mName)
        val locationTextView: TextView = itemView.findViewById(R.id.mObLoc)
        val speciesTextView: TextView = itemView.findViewById(R.id.mSpecies)
        val descriptionTextView: TextView = itemView.findViewById(R.id.mDescription)
    }
}

