package tn.esprit.hollyfood.view.fragments.main.categories

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tn.esprit.hollyfood.viewmodel.PlateViewModel

class PlateCategoryFragment(private val restaurantId: String) : CategoryFragment() {
    private lateinit var viewModel: PlateViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlateViewModel::class.java)
        viewModel.getPlatesByRestaurant(restaurantId)

        viewModel.platesLiveData.observe(viewLifecycleOwner,
            Observer {
                if (it != null) {
                    val pastaPlates = it.filter { it.category == "Plate" }
                    menuAdapter.setList(pastaPlates)
                } else {
                    Toast.makeText(
                        context,
                        "Connection Failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}