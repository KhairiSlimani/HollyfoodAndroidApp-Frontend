package tn.esprit.hollyfood.view.fragments.LoginRegister

import android.content.Context
import android.content.Intent
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
import tn.esprit.hollyfood.databinding.FragmentAccountVerificationBinding
import tn.esprit.hollyfood.databinding.FragmentCodeVerificationBinding
import tn.esprit.hollyfood.view.activities.LoginRegisterActivity
import tn.esprit.hollyfood.view.activities.MainActivity
import tn.esprit.hollyfood.viewmodel.UserViewModel

class AccountVerificationFragment : Fragment(R.layout.fragment_account_verification) {
    private lateinit var binding: FragmentAccountVerificationBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountVerificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val email = arguments?.getString("email") ?: ""

        binding.apply {
            buttonVerify.setOnClickListener {
                buttonVerify.startAnimation()

                viewModel.verifyAccount(email, edCode.text.toString().trim())
            }

            buttonLogout.setOnClickListener {
                val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.clear()
                editor.apply()

                Intent(requireActivity(), LoginRegisterActivity::class.java).also { intent ->
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

            }


            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonVerify.revertAnimation()
                    layoutCode.error = null

                    if (it == "User verified successfully.") {
                        viewModel.getByEmail(email)

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

            viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonVerify.revertAnimation()

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

                    Toast.makeText(
                        context,
                        "Account verified successfully.",
                        Toast.LENGTH_LONG
                    ).show()

                    Intent(requireActivity(), MainActivity::class.java).also { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            })



        }
    }
}