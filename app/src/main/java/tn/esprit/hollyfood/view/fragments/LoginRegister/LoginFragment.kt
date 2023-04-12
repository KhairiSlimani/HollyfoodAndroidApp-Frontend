package tn.esprit.hollyfood.view.fragments.LoginRegister

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.apply {
            tvForgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }

            tvLoginText.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            buttonLogin.setOnClickListener {

                buttonLogin.startAnimation()

                viewModel.login(edEmail.text.toString().trim(), edPassword.text.toString().trim())
            }

            viewModel.userLiveData.observe(viewLifecycleOwner, Observer {

                if (it != null) {
                    buttonLogin.revertAnimation()

                    val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.apply {
                        putString("id", it.id)
                        putString("fullname", it.fullname)
                        putString("email", it.email)
                        putInt("phone", it.phone)
                        putString("role", it.email)
                        putString("image", it.image)
                    }.apply()

                    Intent(requireActivity(), MainActivity::class.java).also { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

                } else {
                    buttonLogin.revertAnimation()

                    Toast.makeText(
                        context,
                        "Connection Failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonLogin.revertAnimation()
                    layoutEmail.error = null
                    layoutPassword.error = null

                    if(it == "The email you entered isn’t connected to an account."){
                        layoutEmail.error = it
                    } else if (it == "The password you’ve entered is incorrect.") {
                        layoutPassword.error = it
                    } else if (it == "Your account has not yet been verified.") {
                        viewModel.clearMessage()
                        val action = LoginFragmentDirections.actionLoginFragmentToAccountVerificationFragment(edEmail.text.toString().trim())
                        findNavController().navigate(action)
                    }
                    else if (it != "") {
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {
                    buttonLogin.revertAnimation()

                    Toast.makeText(
                        context,
                        "Connection Failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

            lifecycleScope.launch {
                viewModel.validation.collect { validation ->
                    layoutEmail.error = null
                    layoutPassword.error = null

                    if (validation.email is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutEmail.error = validation.email.message
                            buttonLogin.revertAnimation()

                        }
                    }

                    if (validation.password is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutPassword.error = validation.password.message
                            buttonLogin.revertAnimation()

                        }
                    }
                }
            }
        }
    }
}