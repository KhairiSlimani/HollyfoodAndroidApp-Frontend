package tn.esprit.hollyfood.view.fragments

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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentRegisterBinding
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.viewmodel.UserViewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.apply {
            tvRegisterText.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }

            buttonRegister.setOnClickListener {
                buttonRegister.startAnimation()

                val phoneNumber = edPhoneNumber.text.toString().trim().toIntOrNull() ?: -1
                val confirmPassword = edConfirmPassword.text.toString().trim()

                val user = User(
                    "0",
                    edFullName.text.toString().trim(),
                    edEmail.text.toString().trim(),
                    edPassword.text.toString().trim(),
                    phoneNumber,
                    "User",
                    ""
                )
                viewModel.register(user, confirmPassword)
            }

            viewModel.userLiveData.observe(viewLifecycleOwner, Observer {

                if (it != null) {
                    buttonRegister.revertAnimation()

                    Toast.makeText(
                        context,
                        "Account created successfully.",
                        Toast.LENGTH_LONG
                    ).show()

                    edFullName.setText("")
                    edEmail.setText("")
                    edPassword.setText("")
                    edConfirmPassword.setText("")
                    edPhoneNumber.setText("")
                }
            })

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonRegister.revertAnimation()
                    layoutEmail.error = null

                    if(it == "Email already exist."){
                        layoutEmail.error = it
                    } else {
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

            lifecycleScope.launch {
                viewModel.validation.collect { validation ->

                    layoutFullName.error = null
                    layoutEmail.error = null
                    layoutPassword.error = null
                    layoutConfirmPassword.error = null
                    layoutPhoneNumber.error = null

                    if (validation.fullname is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutFullName.error = validation.fullname.message
                            buttonRegister.revertAnimation()

                        }
                    }

                    if (validation.email is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutEmail.error = validation.email.message
                            buttonRegister.revertAnimation()
                        }
                    }

                    if (validation.password is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            if(validation.password.message == "Passwords do not match." || validation.password.message == "Confirm your password.") {
                                layoutConfirmPassword.error = validation.password.message
                            } else {
                                layoutPassword.error = validation.password.message
                            }
                            buttonRegister.revertAnimation()
                        }
                    }

                    if (validation.phone is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutPhoneNumber.error = validation.phone.message
                            buttonRegister.revertAnimation()
                        }
                    }
                }
            }
        }
    }
}