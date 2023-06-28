package com.example.adoptionapp.ui.home

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptionapp.R
import com.squareup.picasso.Picasso

class AnimalAdapter(private var animalList: MutableList<Animal>): RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimalViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.animal_card_view, parent, false)
        return AnimalViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        Picasso.get().load(animalList[position].img)
            .into(holder.animalImage)
        holder.animalTitle.text = animalList[position].title
        holder.animalDescription.text = animalList[position].description
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    class AnimalViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var animalImage: ImageView = view.findViewById(R.id.animal_image)
        var animalTitle: TextView = view.findViewById(R.id.animal_title)
        var animalDescription: TextView = view.findViewById(R.id.animal_description)

    }

}