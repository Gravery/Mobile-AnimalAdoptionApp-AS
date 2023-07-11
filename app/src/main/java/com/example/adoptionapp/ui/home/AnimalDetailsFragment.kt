package com.example.adoptionapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adoptionapp.databinding.FragmentAnimalDetailsBinding
import com.squareup.picasso.Picasso

class AnimalDetailsFragment : Fragment() {
    private var _binding: FragmentAnimalDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var animal: Animal

    companion object {
        private const val ARG_ANIMAL = "animal"

        fun newInstance(animal: Animal): AnimalDetailsFragment {
            val fragment = AnimalDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_ANIMAL, animal)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        animal = arguments?.getParcelable(ARG_ANIMAL) ?: Animal("", "", "", "", "", "", "", "", "")

        populateAnimalDetails(animal)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun populateAnimalDetails(animal: Animal) {
        binding.animalName.text = animal.name
        binding.animalDescription.text = animal.description
        binding.animalBreed.text = animal.breed
        binding.animalAge.text = animal.age
        binding.animalSize.text = animal.type
        binding.animalContact.text = animal.contact

        Picasso.get()
            .load(animal.image)
            .into(binding.animalImgDetails)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
