package com.example.adoptionapp.ui.notifications

import AnimalAdAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptionapp.databinding.FragmentNotificationsBinding
import com.example.adoptionapp.ui.home.Animal
import com.example.adoptionapp.ui.home.AnimalAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    var animalList = ArrayList<Animal>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimalAdAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AnimalAdAdapter(animalList, object : AnimalAdAdapter.OnDeleteClickListener {
            override fun onDeleteClick(animal: Animal) {
                deleteAnimalAd(animal)
            }
        })
        recyclerView.adapter = adapter

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("animals")

        val currentUser = auth.currentUser
        currentUser?.uid?.let { userId ->
            val query = database.orderByChild("user").equalTo(userId)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    animalList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val animal = dataSnapshot.getValue(Animal::class.java)
                        animal?.let {
                            val id = it.id
                            val image = it.image
                            val name = it.name
                            val description = it.description
                            val breed = it.breed
                            val age = it.age
                            val type = it.type
                            val contact = it.contact
                            val user = it.user

                            val animalWithId = Animal(id, image, name, description, breed, age, type, contact, user)
                            animalList.add(animalWithId)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Erro", error.message)
                }
            })
        }

        return view
    }

    private fun deleteAnimalAd(animal: Animal) {
        val animalId = animal.id
        database.child(animalId).removeValue()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}