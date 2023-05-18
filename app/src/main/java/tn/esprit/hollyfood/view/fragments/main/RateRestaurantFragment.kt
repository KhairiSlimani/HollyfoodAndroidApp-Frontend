package tn.esprit.hollyfood.view.fragments.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentRateRestaurantBinding
import tn.esprit.hollyfood.model.entities.Rating
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.viewmodel.RatingViewModel
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel

class RateRestaurantFragment : Fragment(R.layout.fragment_rate_restaurant) {
    private lateinit var binding: FragmentRateRestaurantBinding
    private lateinit var viewModel: RatingViewModel

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
        viewModel = ViewModelProvider(this).get(RatingViewModel::class.java)
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userId : String = sharedPref.getString("id", "") ?: ""
        val restaurantId = arguments?.getString("restaurantId") ?: ""

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

            buttonRate.setOnClickListener {
                buttonRate.startAnimation()
                val rating = Rating(
                    "0",
                    ratingBar.rating.toInt(),
                    userId,
                    restaurantId,
                )

                viewModel.addOrUpdateRating(rating)
            }

            viewModel.ratingLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonRate.revertAnimation()
                    Snackbar.make(requireView(), "Your rating has been added.", Snackbar.LENGTH_LONG).show()
                }
            })

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if(it == "Server error, please try again later." || it == "Network error, please try again later.") {
                        buttonRate.revertAnimation()
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                    }
                }
            })

        }
    }
}