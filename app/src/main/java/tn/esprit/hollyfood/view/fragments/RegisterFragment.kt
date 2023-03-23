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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentRegisterBinding
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.viewmodel.UserViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.apply {

            buttonRegister.setOnClickListener {
                buttonRegister.startAnimation()

                val phoneNumber = edPhoneNumber.text.toString().trim().toIntOrNull() ?: -1

                val user = User(
                    "0",
                    edFullName.text.toString().trim(),
                    edEmail.text.toString().trim(),
                    edPassword.text.toString().trim(),
                    phoneNumber,
                    "User",
                    ""
                )
                viewModel.register(user)
            }
        }

        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                binding.buttonRegister.revertAnimation()

                Toast.makeText(
                    context,
                    "Account is added successfully",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                binding.buttonRegister.revertAnimation()

                Toast.makeText(
                    context,
                    "Connection Failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        lifecycleScope.launch {
            viewModel.validation.collect { validation ->

                if (validation.fullname is Validation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            edFullName.apply {
                                requestFocus()
                                error = validation.fullname.message
                            }
                            buttonRegister.revertAnimation()
                        }

                    }
                }

                if (validation.email is Validation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            edEmail.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                            buttonRegister.revertAnimation()
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
                            buttonRegister.revertAnimation()
                        }
                    }
                }

                if (validation.phone is Validation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            edPhoneNumber.apply {
                                requestFocus()
                                error = validation.phone.message
                            }
                            buttonRegister.revertAnimation()
                        }
                    }
                }

            }
        }

    }

}