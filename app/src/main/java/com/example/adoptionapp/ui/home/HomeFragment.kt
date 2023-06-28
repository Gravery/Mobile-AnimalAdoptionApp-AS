package com.example.adoptionapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptionapp.FragmentNavigation
import com.example.adoptionapp.R
import com.example.adoptionapp.databinding.FragmentHomeBinding
import com.example.adoptionapp.ui.login.LoginFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapter: AnimalAdapter? = null
    var animalList = ArrayList<Animal>()
    private var database: FirebaseDatabase? = null
    private var reference: DatabaseReference? = null
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance()
        reference = database?.getReference("animals")

        val firebaseListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                animalList.clear()

                val child = snapshot.children
                child.forEach{
                    var animals = Animal(it.child("img").value.toString(),
                    it.child("title").value.toString(),
                        it.child("description").value.toString())

                    animalList.add(animals)
                }
                    adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("test", error.message)
            }

        }
        reference?.addValueEventListener(firebaseListener)

        recyclerView = requireView().findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

        adapter = AnimalAdapter(animalList)
        recyclerView?.adapter = adapter

        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}