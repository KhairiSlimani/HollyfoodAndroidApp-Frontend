package tn.esprit.hollyfood.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentLoginBinding
import tn.esprit.hollyfood.viewmodel.UserViewModel

class IntroFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

}