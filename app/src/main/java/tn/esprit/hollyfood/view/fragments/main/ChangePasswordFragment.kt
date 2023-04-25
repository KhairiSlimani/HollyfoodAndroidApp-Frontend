package tn.esprit.hollyfood.view.fragments.main

import android.content.Context
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
import tn.esprit.hollyfood.databinding.FragmentChangePasswordBinding
import tn.esprit.hollyfood.databinding.FragmentSettingsBinding
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.view.fragments.LoginRegister.ResetPasswordFragmentDirections
import tn.esprit.hollyfood.viewmodel.UserViewModel

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.apply {
            val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val email = sharedPref.getString("email", "") ?: ""

            buttonConfirm.setOnClickListener {
                buttonConfirm.startAnimation()

                viewModel.changePassword(email, edOldPassword.text.toString().trim(), edNewPassword.text.toString().trim(), edConfirmPassword.text.toString().trim())
            }

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonConfirm.revertAnimation()
                    layoutOldPassword.error = null
                    layoutNewPassword.error = null
                    layoutConfirmPassword.error = null

                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_LONG
                    ).show()

                    if (it == "Password changed successfully.") {
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (it == "Wrong password.") {
                        layoutOldPassword.error = it
                    }
                }
            })

            lifecycleScope.launch {
                viewModel.userValidation.collect { validation ->
                    layoutOldPassword.error = null
                    layoutNewPassword.error = null
                    layoutConfirmPassword.error = null

                    if (validation.password is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            if(validation.password.message == "Passwords do not match." || validation.password.message == "Confirm your password.") {
                                layoutConfirmPassword.error = validation.password.message
                            } else {
                                layoutNewPassword.error = validation.password.message
                            }
                            buttonConfirm.revertAnimation()
                        }
                    }

                }
            }


        }
    }
}