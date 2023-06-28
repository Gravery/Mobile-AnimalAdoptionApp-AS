package com.example.adoptionapp.ui.login

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
import com.example.adoptionapp.FragmentNavigation
import com.example.adoptionapp.R
import com.example.adoptionapp.ui.home.HomeFragment
import com.example.adoptionapp.ui.register.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LoginFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button
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
        var view = inflater.inflate(R.layout.fragment_login, container, false)

        username = view.findViewById(R.id.log_username)
        password = view.findViewById(R.id.log_password)
        btnLogin = view.findViewById(R.id.btn_login)
        fAuth = Firebase.auth

        view.findViewById<Button>(R.id.btn_register).setOnClickListener {
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(RegisterFragment(), false)
        }

        view.findViewById<Button>(R.id.btn_login).setOnClickListener {
            validateForm()
        }

        return view
    }

    private fun firebaseSignIn() {
        btnLogin.isEnabled = false
        btnLogin.alpha = 0.5f
        fAuth.signInWithEmailAndPassword(username.text.toString(), password.text.toString()).addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login Concluído",  Toast.LENGTH_SHORT).show()
                var navHome = activity as FragmentNavigation
                navHome.navigateFrag(HomeFragment(), true)
            } else {
                btnLogin.isEnabled = true
                btnLogin.alpha = 1.0f
                Toast.makeText(context, task.exception?.message,  Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm() {
        val icon = AppCompatResources.getDrawable(requireContext(),
            R.drawable.baseline_warning_24)

        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(username.text.toString().trim())->{
                username.setError("Insira um Usuário", icon)
            }
            TextUtils.isEmpty(password.text.toString().trim())->{
                password.setError("Insira uma Senha", icon)
            }

            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() ->
            {
                if (username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    firebaseSignIn()
                } else {
                    username.setError("Insira um Nome Válido", icon)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}