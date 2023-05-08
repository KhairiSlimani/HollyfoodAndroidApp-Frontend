package tn.esprit.hollyfood.view.fragments.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentMyRestaurantsBinding
import tn.esprit.hollyfood.databinding.FragmentOrdersBinding
import tn.esprit.hollyfood.model.entities.Order
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.view.adapters.OnListItemClick
import tn.esprit.hollyfood.view.adapters.OrdersAdapter
import tn.esprit.hollyfood.view.adapters.RestaurantRecyclerView
import tn.esprit.hollyfood.viewmodel.OrderViewModel
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OrdersFragment : Fragment(R.layout.fragment_orders), OnListItemClick {
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var viewModel: OrderViewModel
    val ordersAdapter: OrdersAdapter by lazy {
        OrdersAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        val restaurantId = arguments?.getString("restaurantId") ?: ""
        binding.apply {
            recyclerView.adapter = ordersAdapter
            viewModel.getOrdersByRestaurant(restaurantId)
        }

        ordersAdapter.onListItemClick = this

        binding.apply {
            viewModel.ordersLiveData.observe(viewLifecycleOwner,
                Observer {
                    if (it != null) {
                        ordersAdapter.setList(it)
                    } else {
                        Toast.makeText(
                            context,
                            "Connection Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if(it == "Server error, please try again later."){
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

        }

    }

    override fun onDeleteOrder(order: Order) {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Order")
            setMessage("Do you want to delete this order?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteOrder(order.id)
                viewModel.getOrdersByRestaurant(order.restaurantId)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    override fun onItemClick(restaurant: Restaurant) {
        TODO("Not yet implemented")
    }

    override fun onAddToCartClick(plate: Plate) {
        TODO("Not yet implemented")
    }

}