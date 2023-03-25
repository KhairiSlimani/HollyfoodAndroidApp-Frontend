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
import tn.esprit.hollyfood.databinding.FragmentForgotPasswordBinding
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.viewmodel.UserViewModel

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.apply {

            buttonSend.setOnClickListener {
                buttonSend.startAnimation()

                viewModel.forgotPassword(edEmail.text.toString().trim())
            }
        }

        viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.buttonSend.revertAnimation()

                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_LONG
                ).show()

                if (it == "Reset password code sent successfully.") {
                    viewModel.clearMessage()
                    var action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToCodeVerificationFragment(binding.edEmail.text.toString().trim())
                    findNavController().navigate(action)
                }
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
                            buttonSend.revertAnimation()
                        }
                    }
                }
            }
        }

    }
}