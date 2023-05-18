package tn.esprit.hollyfood.view.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val sharedPref = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val role : String = sharedPref.getString("role", "") ?: ""

        val navController = findNavController(R.id.mainHostFragment)

        val menuResId = if (role == "User") {
            R.menu.user_bottom_navigation
        } else {
            R.menu.restaurant_manager_bottom_navigation
        }

        binding.bottomNavigation.inflateMenu(menuResId)
        binding.bottomNavigation.setupWithNavController(navController)
    }
}