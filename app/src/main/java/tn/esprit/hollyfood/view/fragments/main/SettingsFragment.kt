package tn.esprit.hollyfood.view.fragments.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentForgotPasswordBinding
import tn.esprit.hollyfood.databinding.FragmentSettingsBinding
import tn.esprit.hollyfood.view.activities.LoginRegisterActivity
import tn.esprit.hollyfood.viewmodel.UserViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val fullname : String? = sharedPref.getString("fullname", "")

        binding.apply {
            constraintProfile.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
            }

            tvFullName.text = fullname

            linearLogout.setOnClickListener {
                val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            linearChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_changePasswordFragment)

            }

        }
    }

}