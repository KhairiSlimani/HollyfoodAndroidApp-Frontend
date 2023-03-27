package tn.esprit.hollyfood.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentCodeVerificationBinding
import tn.esprit.hollyfood.databinding.FragmentForgotPasswordBinding
import tn.esprit.hollyfood.viewmodel.UserViewModel

class CodeVerificationFragment : Fragment(R.layout.fragment_code_verification) {
    private lateinit var binding: FragmentCodeVerificationBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCodeVerificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val email = arguments?.getString("email") ?: ""

        binding.apply {
            tvText2.text = email

            buttonContinue.setOnClickListener {
                buttonContinue.startAnimation()

                viewModel.codeVerification(edCode.text.toString().trim())
            }

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonContinue.revertAnimation()
                    layoutCode.error = null

                    if (it == "Valid code.") {
                        var action =
                            CodeVerificationFragmentDirections.actionCodeVerificationFragmentToResetPasswordFragment(
                                email
                            )
                        findNavController().navigate(action)

                    } else if (it == "The code you entered doesn't match your code. Please try again.") {
                        layoutCode.error = it
                    } else if (it != "") {
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        }
    }
}