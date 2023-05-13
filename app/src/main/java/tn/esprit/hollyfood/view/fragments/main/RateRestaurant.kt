package tn.esprit.hollyfood.view.fragments.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentRateRestaurantBinding

class RateRestaurant : Fragment(R.layout.fragment_rate_restaurant) {
    private lateinit var binding: FragmentRateRestaurantBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRateRestaurantBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            ratingBar.setOnRatingBarChangeListener{ ratingBar, fl, b ->
                ratingText.text = fl.toString()
                when(ratingBar.rating.toInt()){
                    1 -> ratingText.text = "Very Bad"
                    2 -> ratingText.text = "Bad"
                    3 -> ratingText.text = "Good"
                    4 -> ratingText.text = "Great"
                    5 -> ratingText.text = "Awesome"
                    else -> ratingText.text = " "
                }
            }

            buttonRate.setOnClickListener {  }
        }
    }
}