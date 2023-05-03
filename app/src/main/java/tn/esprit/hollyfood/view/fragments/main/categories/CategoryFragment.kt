package tn.esprit.hollyfood.view.fragments.main.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentCategoryBinding
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.view.adapters.MenuAdapter
import tn.esprit.hollyfood.view.adapters.OnListItemClick

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
        Toast.makeText(
            context,
            plate.id,
            Toast.LENGTH_LONG
        ).show()

    }


    override fun onItemClick(restaurant: Restaurant) {
        TODO("Not yet implemented")
    }

}