package tn.esprit.hollyfood.view.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.model.entities.Order

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>(){
    var ordersList:List<Order> = emptyList()
    var onListItemClick: OnListItemClick? = null

    fun setList(ordersList: List<Order>) {
        this.ordersList = ordersList
        notifyDataSetChanged()
    }

    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemOrderRestaurant: TextView = itemView.findViewById(R.id.itemOrderRestaurant)
        var itemOrderDate: TextView = itemView.findViewById(R.id.itemOrderDate)
        var itemOrderPrice: TextView = itemView.findViewById(R.id.itemOrderPrice)
        var buttonDelete: ImageView = itemView.findViewById(R.id.buttonDelete)

        fun bind(order: Order){
            itemOrderRestaurant.text = order.restaurantName
            itemOrderDate.text = order.date
            itemOrderPrice.text = order.price.toString()

            buttonDelete.setOnClickListener{
                onListItemClick?.onDeleteOrder(order)
            }

            itemView.setOnClickListener{
                onListItemClick?.onOrderClick(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        var order: Order = ordersList.get(position)
        holder.bind(order)
    }
}