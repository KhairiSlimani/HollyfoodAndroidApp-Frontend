package tn.esprit.hollyfood.view.fragments.main.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentCategoryBinding
import tn.esprit.hollyfood.model.entities.Orderline
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.view.adapters.MenuAdapter
import tn.esprit.hollyfood.view.adapters.OnListItemClick
import tn.esprit.hollyfood.viewmodel.OrderViewModel
import tn.esprit.hollyfood.viewmodel.OrderlineViewModel
import tn.esprit.hollyfood.viewmodel.PlateViewModel
import java.io.Console

open class CategoryFragment() : Fragment(R.layout.fragment_category), OnListItemClick {
    private lateinit var binding: FragmentCategoryBinding

    protected val menuAdapter: MenuAdapter by lazy {
        MenuAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuAdapter.onListItemClick = this
        binding.apply {
            rvMenu.adapter = menuAdapter
        }
    }

    override fun onAddToCartClick(plate: Plate) {
        val orderline = Orderline("", 1, plate.price, plate.id, "", plate.name, plate.category, plate.image, plate.price)
        OrderlineViewModel.cart.add(orderline)
        OrderlineViewModel.calculateTotal()
    }


    override fun onItemClick(restaurant: Restaurant) {
        TODO("Not yet implemented")
    }
}