package tn.esprit.hollyfood.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentLoginBinding
import tn.esprit.hollyfood.databinding.FragmentRegisterBinding
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.view.activities.MainActivity
import tn.esprit.hollyfood.viewmodel.UserViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.tvLoginText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.apply {

            buttonLogin.setOnClickListener {

                buttonLogin.startAnimation()

                viewModel.login(edEmail.text.toString().trim(), edPassword.text.toString().trim())
            }
        }

        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                binding.buttonLogin.revertAnimation()

                Toast.makeText(
                    context,
                    "User ${it.fullname} logged in",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                binding.buttonLogin.revertAnimation()

                Toast.makeText(
                    context,
                    "Connection Failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                binding.buttonLogin.revertAnimation()

                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_LONG
                ).show()

            } else {
                binding.buttonLogin.revertAnimation()

                Toast.makeText(
                    context,
                    "Connection Failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        lifecycleScope.launch {
            viewModel.validation.collect { validation ->

                if (validation.email is Validation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            edEmail.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                            buttonLogin.revertAnimation()
                        }
                    }
                }

                if (validation.password is Validation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            edPassword.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                            buttonLogin.revertAnimation()
                        }
                    }
                }

            }
        }
    }

}