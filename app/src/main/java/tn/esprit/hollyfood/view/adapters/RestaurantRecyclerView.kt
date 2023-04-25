package tn.esprit.hollyfood.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.model.entities.Restaurant

class RestaurantRecyclerView: RecyclerView.Adapter<RestaurantRecyclerView.RestaurantViewHolder>() {
    var restaurantsList:List<Restaurant> = emptyList()
    var onListItemClick: OnListItemClick? = null

    fun setList(restaurantsList: List<Restaurant>) {
        this.restaurantsList = restaurantsList
        notifyDataSetChanged()
    }

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        var itemName: TextView = itemView.findViewById(R.id.itemName)
        var itemDescription: TextView = itemView.findViewById(R.id.itemDescription)

        fun bind(restaurant: Restaurant){
            itemImage.setImageResource(R.drawable.restaurant_item)
            itemName.text = restaurant.name
            itemDescription.text = restaurant.description

            itemView.setOnClickListener{
                onListItemClick?.onItemClick(restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantsList.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        var restaurant: Restaurant = restaurantsList.get(position)
        holder.bind(restaurant)
    }

}