package tn.esprit.hollyfood.view.fragments.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentRestaurantMenuBinding
import tn.esprit.hollyfood.view.adapters.MenuViewpagerAdapter
import tn.esprit.hollyfood.view.fragments.main.categories.*

class RestaurantMenuFragment : Fragment(R.layout.fragment_restaurant_menu) {
    private lateinit var binding: FragmentRestaurantMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val restaurantId = arguments?.getString("restaurantId") ?: ""
        val restaurantName = arguments?.getString("restaurantName") ?: ""
        val restaurantUserId = arguments?.getString("restaurantUserId") ?: ""

        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userId : String = sharedPref.getString("id", "") ?: ""

        binding.apply {

            if(restaurantUserId != userId){
                tvAddPlate.visibility = View.GONE
            }

            buttonCart.setOnClickListener {
                val action = RestaurantMenuFragmentDirections.actionRestaurantMenuFragmentToCartFragment(restaurantId, restaurantName)
                findNavController().navigate(action)
            }

            tvAddPlate.setOnClickListener {
                val action = RestaurantMenuFragmentDirections.actionRestaurantMenuFragmentToAddPlateFragment(restaurantId)
                findNavController().navigate(action)
            }

            val MenuCategoriesFragment = arrayListOf<Fragment>(
                AllCategoriesFragment(restaurantId),
                PizzaCategoryFragment(restaurantId),
                PastaCategoryFragment(restaurantId),
                SandwichCategoryFragment(restaurantId),
                PlateCategoryFragment(restaurantId),
                OtherCategoryFragment(restaurantId)
            )

            val menuViewpagerAdapter = MenuViewpagerAdapter(MenuCategoriesFragment, childFragmentManager, lifecycle)
            viewpagerMenu.adapter = menuViewpagerAdapter
            TabLayoutMediator(binding.tabLayout, binding.viewpagerMenu) { tab, position ->
                when(position) {
                    0 -> tab.text = "All"
                    1 -> tab.text = "Pizza"
                    2 -> tab.text = "Pasta"
                    3 -> tab.text = "Sandwich"
                    4 -> tab.text = "Plate"
                    5 -> tab.text = "Other"
                }
            }.attach()
        }
    }
}