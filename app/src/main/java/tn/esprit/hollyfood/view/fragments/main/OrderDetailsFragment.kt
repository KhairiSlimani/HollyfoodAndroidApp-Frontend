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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentOrderDetailsBinding
import tn.esprit.hollyfood.databinding.FragmentRestaurantDetailsBinding
import tn.esprit.hollyfood.view.adapters.OrderlinesAdapter
import tn.esprit.hollyfood.view.adapters.RestaurantRecyclerView
import tn.esprit.hollyfood.viewmodel.OrderViewModel
import tn.esprit.hollyfood.viewmodel.OrderlineViewModel
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel

class OrderDetailsFragment : Fragment(R.layout.fragment_order_details) {
    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderlineViewModel: OrderlineViewModel
    val orderlinesAdapter: OrderlinesAdapter by lazy {
        OrderlinesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderlineViewModel = ViewModelProvider(this).get(OrderlineViewModel::class.java)
        val id = arguments?.getString("orderId") ?: ""


        binding.apply {
            recyclerView.adapter = orderlinesAdapter

            orderViewModel.getOrderById(id)

            orderViewModel.orderLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    date.setText(it.date)
                    address.setText(it.address)
                    phoneNumber.setText(it.phoneNumber.toString())
                    price.setText(it.price.toString())

                    orderlineViewModel.getOrderlinesByOrder(it.id)

                }
            })

            orderlineViewModel.orderlinesLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    orderlinesAdapter.setList(it)
                } else {
                    Toast.makeText(
                        context,
                        "Connection Failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })


        }

    }
}