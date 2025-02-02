package tn.esprit.hollyfood.view.fragments.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentCartBinding
import tn.esprit.hollyfood.view.adapters.CartAdapter
import tn.esprit.hollyfood.viewmodel.OrderlineViewModel

class CartFragment : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    val cartAdapter: CartAdapter by lazy {
        CartAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userId : String = sharedPref.getString("id", "") ?: ""
        val restaurantId = arguments?.getString("restaurantId") ?: ""
        val restaurantName = arguments?.getString("restaurantName") ?: ""

        binding.apply {
            rvCart.layoutManager = LinearLayoutManager(requireContext())
            rvCart.adapter = cartAdapter
            cartAdapter.setList(OrderlineViewModel.cart)

            OrderlineViewModel.totalLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    tvTotalPrice.text = it.toString()
                }
            })

            buttonCheckOut.setOnClickListener {
                val action = CartFragmentDirections.actionCartFragmentToBillingFragment(restaurantId, restaurantName)
                findNavController().navigate(action)
            }

        }
    }
}