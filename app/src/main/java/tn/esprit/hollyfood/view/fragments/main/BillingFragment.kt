package tn.esprit.hollyfood.view.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentBillingBinding

class BillingFragment : Fragment(R.layout.fragment_billing) {
    private lateinit var binding: FragmentBillingBinding

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

    }
}