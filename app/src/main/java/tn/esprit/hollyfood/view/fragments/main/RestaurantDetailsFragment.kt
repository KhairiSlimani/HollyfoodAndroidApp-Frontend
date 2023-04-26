package tn.esprit.hollyfood.view.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentProfileBinding
import tn.esprit.hollyfood.databinding.FragmentRestaurantDetailsBinding
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel
import tn.esprit.hollyfood.viewmodel.UserViewModel

class RestaurantDetailsFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentRestaurantDetailsBinding
    private lateinit var viewModel: RestaurantViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        binding.apply {


        }
    }
}