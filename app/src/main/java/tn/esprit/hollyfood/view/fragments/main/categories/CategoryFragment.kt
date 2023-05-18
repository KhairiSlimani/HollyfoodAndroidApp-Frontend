package tn.esprit.hollyfood.view.fragments.main.categories

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentCategoryBinding
import tn.esprit.hollyfood.model.entities.Order
import tn.esprit.hollyfood.model.entities.Orderline
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.view.adapters.MenuAdapter
import tn.esprit.hollyfood.view.adapters.OnListItemClick
import tn.esprit.hollyfood.view.fragments.main.MyRestaurantsFragmentDirections
import tn.esprit.hollyfood.view.fragments.main.RestaurantMenuFragmentDirections
import tn.esprit.hollyfood.viewmodel.OrderViewModel
import tn.esprit.hollyfood.viewmodel.OrderlineViewModel
import tn.esprit.hollyfood.viewmodel.PlateViewModel
import java.io.Console

open class CategoryFragment() : Fragment(R.layout.fragment_category), OnListItemClick {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var builder: AlertDialog.Builder
    private lateinit var viewModel: PlateViewModel

    protected val menuAdapter: MenuAdapter by lazy {
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userId : String = sharedPref.getString("id", "") ?: ""
        MenuAdapter(userId)
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
        builder = AlertDialog.Builder(requireContext())
        viewModel = ViewModelProvider(this).get(PlateViewModel::class.java)

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

    override fun onEditPlate(plate: Plate) {
        val action = RestaurantMenuFragmentDirections.actionRestaurantMenuFragmentToEditPlateFragment(plate.id)
        findNavController().navigate(action)
    }

    override fun onDeletePlate(plate: Plate) {
        builder.setTitle("Alert!")
            .setMessage("Do you really want to delete this plate?")
            .setCancelable(true)
            .setPositiveButton("Yes"){dialogInterface, it ->
                viewModel.deletePlate(plate.id)
            }
            .setNegativeButton("No"){dialogInterface, it ->
                dialogInterface.cancel()
            }
            .show()

    }

    override fun onOrderClick(order: Order) {
        TODO("Not yet implemented")
    }

    override fun onDeleteOrder(order: Order) {
        TODO("Not yet implemented")
    }


    override fun onItemClick(restaurant: Restaurant) {
        TODO("Not yet implemented")
    }
}