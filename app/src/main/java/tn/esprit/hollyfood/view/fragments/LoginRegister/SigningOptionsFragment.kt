package tn.esprit.hollyfood.view.fragments.LoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentIntroBinding
import tn.esprit.hollyfood.databinding.FragmentSigningOptionsBinding

class SigningOptionsFragment : Fragment(R.layout.fragment_signing_options) {
    private lateinit var binding: FragmentSigningOptionsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigningOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegisterOption.setOnClickListener {
            findNavController().navigate(R.id.action_signingOptionsFragment_to_registerFragment)
        }

        binding.buttonLoginOption.setOnClickListener {
            findNavController().navigate(R.id.action_signingOptionsFragment_to_loginFragment)
        }
    }

}