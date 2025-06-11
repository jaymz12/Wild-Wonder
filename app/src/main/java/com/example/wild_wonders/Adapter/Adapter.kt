package com.example.wild_wonders.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wild_wonders.R

class Adapter (val c: Context, val userList:ArrayList<AddDetailsData>):RecyclerView.Adapter<Adapter.UserViewHolder>() {

    inner class UserViewHolder(val v: View): RecyclerView.ViewHolder(v){
        var name: TextView
        var mObLoc: TextView
        var mSpecies: TextView
        var mDescription: TextView

        init {
            name = v.findViewById<TextView>(R.id.mName)
            mObLoc = v.findViewById<TextView>(R.id.mObLoc)
            mSpecies = v.findViewById<TextView>(R.id.mSpecies)
            mDescription = v.findViewById<TextView>(R.id.mDescription)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.display,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val newList = userList[position]
        holder.name.text = newList.name
        holder.mObLoc.text = newList.observationLocation
        holder.mSpecies.text = newList.species
        holder.mDescription.text = newList.description
    }

    override fun getItemCount(): Int {
        return  userList.size
    }
}