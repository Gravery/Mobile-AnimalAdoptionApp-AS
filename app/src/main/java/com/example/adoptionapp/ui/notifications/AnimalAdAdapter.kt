import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptionapp.R
import com.example.adoptionapp.ui.home.Animal
import com.squareup.picasso.Picasso

class AnimalAdAdapter(
    private val animalList: MutableList<Animal>,
    private val onDeleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<AnimalAdAdapter.AnimalAdViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(animal: Animal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalAdViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal_ad, parent, false)
        return AnimalAdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalAdViewHolder, position: Int) {
        val animal = animalList[position]

        Picasso.get()
            .load(animal.image)
            .placeholder(R.drawable.baseline_add_photo_alternate_24)
            .into(holder.animalImage)
        holder.animalTitle.text = animal.name
        holder.animalDescription.text = animal.description

        holder.deleteButton.setOnClickListener {
            onDeleteClickListener.onDeleteClick(animal)
        }
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    inner class AnimalAdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animalImage: ImageView = itemView.findViewById(R.id.animal_image)
        val animalTitle: TextView = itemView.findViewById(R.id.animal_title)
        val animalDescription: TextView = itemView.findViewById(R.id.animal_description)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }
}
