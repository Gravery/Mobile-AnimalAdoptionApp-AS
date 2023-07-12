package com.example.adoptionapp.ui.dashboard

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.adoptionapp.R
import com.example.adoptionapp.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var breed: EditText
    private lateinit var age: EditText
    private lateinit var type: EditText
    private lateinit var contact: EditText
    private lateinit var image: ImageView
    private lateinit var buttonSelect: Button
    private lateinit var button: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        name = root.findViewById(R.id.reg_animal)
        description = root.findViewById(R.id.reg_description)
        breed = root.findViewById(R.id.reg_breed)
        age = root.findViewById(R.id.reg_age)
        type = root.findViewById(R.id.reg_type)
        contact = root.findViewById(R.id.reg_contact)
        image = root.findViewById(R.id.reg_photo)
        buttonSelect = root.findViewById(R.id.btn_add_img)
        button = root.findViewById(R.id.btn_announce)

        database = FirebaseDatabase.getInstance()
        databaseRef = database.reference
        storageRef = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()

        buttonSelect.setOnClickListener {
            openGallery()
        }

        button.setOnClickListener {
            val namestr = name.text.toString()
            val descriptionstr = description.text.toString()
            val breedstr = breed.text.toString()
            val agestr = age.text.toString()
            val typestr = type.text.toString()
            val contactstr = contact.text.toString()
            val currentUser = auth.currentUser

            if (currentUser != null) {
                val userId = currentUser.uid

                if (validateEmptyForm()) {
                    val userRef = databaseRef.child("animals").push()
                    val uuid = UUID.randomUUID()

                    userRef.child("id").setValue(userRef.key)
                    userRef.child("name").setValue(namestr)
                    userRef.child("description").setValue(descriptionstr)
                    userRef.child("breed").setValue(breedstr)
                    userRef.child("age").setValue(agestr)
                    userRef.child("type").setValue(typestr)
                    userRef.child("contact").setValue(contactstr)
                    userRef.child("user").setValue(userId)

                    if (imageUri != null) {
                        val path = "images/${uuid}"
                        val imageRef = storageRef.child(path)
                        val uploadTask = imageRef.putFile(imageUri!!)
                        uploadTask.addOnSuccessListener { uploadTaskSnapshot ->
                            uploadTaskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
                                val imageUrl = downloadUri.toString()
                                userRef.child("image").setValue(imageUrl)
                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, getString(R.string.image_failed), Toast.LENGTH_SHORT).show()
                        }
                    }

                    name.text.clear()
                    description.text.clear()
                    breed.text.clear()
                    age.text.clear()
                    type.text.clear()
                    contact.text.clear()
                    Toast.makeText(context, getString(R.string.publish_created), Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(context, getString(R.string.error_user_not_logged), Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun validateEmptyForm(): Boolean {
        val icon = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.baseline_warning_24
        )

        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(name.text.toString().trim()) -> {
                name.setError(getString(R.string.insert_name), icon)
                return false
            }

            TextUtils.isEmpty(breed.text.toString().trim()) -> {
                breed.setError(getString(R.string.insert_breed), icon)
                return false
            }

            TextUtils.isEmpty(age.text.toString().trim()) -> {
                age.setError(getString(R.string.insert_age), icon)
                return false
            }

            TextUtils.isEmpty(type.text.toString().trim()) -> {
                type.setError(getString(R.string.insert_type), icon)
                return false
            }

            TextUtils.isEmpty(contact.text.toString().trim()) -> {
                contact.setError(getString(R.string.insert_contact), icon)
                return false
            }
        }
        return true
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}