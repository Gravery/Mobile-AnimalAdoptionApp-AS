package com.example.adoptionapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import com.example.adoptionapp.FragmentNavigation
import com.example.adoptionapp.MainActivity
import com.example.adoptionapp.R
import com.example.adoptionapp.ui.home.HomeFragment
import com.example.adoptionapp.ui.login.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RegisterFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var btnRegisterReg: Button
    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_register, container, false)

        username = view.findViewById(R.id.reg_username)
        password = view.findViewById(R.id.reg_password)
        confirmPassword = view.findViewById(R.id.reg_confirm_password)
        btnRegisterReg = view.findViewById(R.id.btn_register_reg)
        fAuth = Firebase.auth


        view.findViewById<Button>(R.id.btn_login_reg).setOnClickListener {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_navigation_register_to_navigation_login)
        }

        view.findViewById<Button>(R.id.btn_register_reg).setOnClickListener {
            validateEmptyForm()
        }
        return view
    }

    private fun validateEmptyForm(){
        val icon = AppCompatResources.getDrawable(requireContext(),
        R.drawable.baseline_warning_24)

        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
       when {
           TextUtils.isEmpty(username.text.toString().trim())->{
               username.setError(getString(R.string.insert_email), icon)
           }
           TextUtils.isEmpty(password.text.toString().trim())->{
               password.setError(getString(R.string.insert_password), icon)
           }
           TextUtils.isEmpty(confirmPassword.text.toString().trim())->{
               confirmPassword.setError(getString(R.string.password_confirm), icon)
           }

           username.text.toString().isNotEmpty() &&
                   password.text.toString().isNotEmpty() &&
                        confirmPassword.text.toString().isNotEmpty() ->
           {
               if (username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    if (password.text.toString().length >= 8) {
                        if (confirmPassword.text.toString() == password.text.toString()) {
                            firebaseSignup()
                        } else {
                            confirmPassword.setError(getString(R.string.password_not_equal), icon)
                        }

                    } else {
                        password.setError(getString(R.string.password_length), icon)
                    }
               } else {
                   username.setError(getString(R.string.valid_email), icon)
               }
           }
       }
    }

    private fun firebaseSignup() {
        btnRegisterReg.isEnabled = false
        btnRegisterReg.alpha = 0.5f
        fAuth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString()).addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                Toast.makeText(context, getString(R.string.register_successful), Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                btnRegisterReg.isEnabled = true
                btnRegisterReg.alpha = 1.0f
                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}