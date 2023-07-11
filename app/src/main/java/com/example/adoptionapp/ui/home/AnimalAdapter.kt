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
    private var clickListener: OnAnimalClickListener? = null
    fun setOnAnimalClickListener(listener: OnAnimalClickListener) {
        clickListener = listener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimalViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.animal_card_view, parent, false)
        return AnimalViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animalList[position]
        Picasso.get().load(animalList[position].image)
            .into(holder.animalImage)
        holder.animalTitle.text = animalList[position].name
        holder.animalDescription.text = animalList[position].description

        holder.itemView.setOnClickListener {
            clickListener?.onAnimalClick(animal)
        }
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    interface OnAnimalClickListener {
        fun onAnimalClick(animal: Animal)
    }
    class AnimalViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var animalImage: ImageView = view.findViewById(R.id.animal_image)
        var animalTitle: TextView = view.findViewById(R.id.animal_title)
        var animalDescription: TextView = view.findViewById(R.id.animal_description)

        fun bind(animal: Animal, clickListener: OnAnimalClickListener) {
            itemView.setOnClickListener {
                clickListener.onAnimalClick(animal)
            }
        }
    }
}