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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentProfileBinding
import tn.esprit.hollyfood.databinding.FragmentSettingsBinding
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.viewmodel.UserViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.apply {
            val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val id : String = sharedPref.getString("id", "") ?: ""
            val fullname : String? = sharedPref.getString("fullname", "")
            val email = sharedPref.getString("email", "")
            val phoneNumber = sharedPref.getInt("phone", 0)

            edFullName.setText(fullname)
            edEmail.setText(email)
            edPhoneNumber.setText(phoneNumber.toString())

            buttonSave.setOnClickListener {
                buttonSave.startAnimation()

                val phoneNumber = edPhoneNumber.text.toString().trim().toIntOrNull() ?: -1

                viewModel.editProfile(id, edFullName.text.toString().trim(), edEmail.text.toString().trim(), phoneNumber)
            }

            viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonSave.revertAnimation()

                    Toast.makeText(
                        context,
                        "Profile edited successfully.",
                        Toast.LENGTH_LONG
                    ).show()

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

                    edFullName.setText(it.fullname)
                    edEmail.setText(it.email)
                    edPhoneNumber.setText(it.phone.toString())
                }
            })

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonSave.revertAnimation()
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
                    layoutPhoneNumber.error = null

                    if (validation.fullname is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutFullName.error = validation.fullname.message
                            buttonSave.revertAnimation()
                        }
                    }

                    if (validation.email is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutEmail.error = validation.email.message
                            buttonSave.revertAnimation()
                        }
                    }

                    if (validation.phone is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutPhoneNumber.error = validation.phone.message
                            buttonSave.revertAnimation()
                        }
                    }
                }
            }


        }
    }
}