package tn.esprit.hollyfood.view.fragments.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentBillingBinding
import tn.esprit.hollyfood.model.entities.Order
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.view.adapters.BillingAdapter
import tn.esprit.hollyfood.view.adapters.CartAdapter
import tn.esprit.hollyfood.viewmodel.OrderViewModel
import tn.esprit.hollyfood.viewmodel.OrderlineViewModel
import tn.esprit.hollyfood.viewmodel.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BillingFragment : Fragment(R.layout.fragment_billing) {
    private lateinit var binding: FragmentBillingBinding
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderlineViewModel: OrderlineViewModel

    val billingAdapter: BillingAdapter by lazy {
        BillingAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderlineViewModel = ViewModelProvider(this).get(OrderlineViewModel::class.java)
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userId : String = sharedPref.getString("id", "") ?: ""
        val phone : Int = sharedPref.getInt("phone", 0) ?: 0

        val restaurantId = arguments?.getString("restaurantId") ?: ""
        val restaurantName = arguments?.getString("restaurantName") ?: ""

        binding.apply {
            rvOrderlines.adapter = billingAdapter
            billingAdapter.setList(OrderlineViewModel.cart)
            edPhoneNumber.setText(phone.toString())

            OrderlineViewModel.totalLiveData.observe(viewLifecycleOwner, Observer {

                if (it != null) {
                    tvTotalPrice.text = it.toString()
                }
            })

            buttonMakeOrder.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Make Order")
                    setMessage("Do you want to order your cart plates?")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes") { dialog, _ ->
                        buttonMakeOrder.startAnimation()

                        val phoneNumber = edPhoneNumber.text.toString().trim().toIntOrNull() ?: -1
                        val currentDate = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formattedDate = currentDate.format(formatter)

                        val order = Order(
                            "0",
                            tvTotalPrice.text.toString().trim().toFloat(),
                            edAddress.text.toString().trim(),
                            phoneNumber,
                            formattedDate,
                            userId,
                            restaurantId,
                            restaurantName
                        )

                        orderViewModel.addOrder(order)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }

            orderViewModel.orderLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    orderlineViewModel.addOrderlines(it)
                }
            })

            lifecycleScope.launch {
                orderViewModel.orderValidation.collect { validation ->

                    layoutPhoneNumber.error = null
                    layoutAddress.error = null

                    if (validation.phoneNumber is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutPhoneNumber.error = validation.phoneNumber.message
                            buttonMakeOrder.revertAnimation()
                        }
                    }

                    if (validation.address is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutAddress.error = validation.address.message
                            buttonMakeOrder.revertAnimation()
                        }
                    }
                }
            }

            orderlineViewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if(it == "Success."){
                        buttonMakeOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(), "Your order was placed", Snackbar.LENGTH_LONG).show()
                    } else if(it == "Server error, please try again later." || it == "Network error, please try again later.") {
                        buttonMakeOrder.revertAnimation()
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                    }
                }
            })

            orderViewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if(it == "Server error, please try again later." || it == "Network error, please try again later.") {
                        buttonMakeOrder.revertAnimation()
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                    }
                }
            })


        }
    }
}