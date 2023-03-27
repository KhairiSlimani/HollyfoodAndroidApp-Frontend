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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentCodeVerificationBinding
import tn.esprit.hollyfood.databinding.FragmentResetPasswordBinding
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.viewmodel.UserViewModel

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val email = arguments?.getString("email") ?: ""

        binding.apply {
            buttonConfirm.setOnClickListener {
                buttonConfirm.startAnimation()

                viewModel.resetPassword(email, edPassword.text.toString().trim(), edConfirmPassword.text.toString().trim())
            }

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonConfirm.revertAnimation()
                    layoutPassword.error = null
                    layoutConfirmPassword.error = null

                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_LONG
                    ).show()

                    if (it == "Password reset successfully.") {
                        var action = ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment2()
                        findNavController().navigate(action)
                    }
                }
            })

            lifecycleScope.launch {
                viewModel.validation.collect { validation ->
                    layoutPassword.error = null
                    layoutConfirmPassword.error = null

                    if (validation.password is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            if(validation.password.message == "Passwords do not match." || validation.password.message == "Confirm your password.") {
                                layoutConfirmPassword.error = validation.password.message
                            } else {
                                layoutPassword.error = validation.password.message
                            }
                            buttonConfirm.revertAnimation()
                        }
                    }

                }
            }
        }
    }
}