package tn.esprit.hollyfood.view.fragments.LoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentIntroBinding
import tn.esprit.hollyfood.databinding.FragmentLoginBinding
import tn.esprit.hollyfood.viewmodel.UserViewModel

class IntroFragment : Fragment(R.layout.fragment_intro) {
    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_signingOptionsFragment)
        }
    }

}
