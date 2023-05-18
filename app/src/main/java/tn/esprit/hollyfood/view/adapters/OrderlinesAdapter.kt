package tn.esprit.hollyfood.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.model.entities.Orderline

class OrderlinesAdapter : RecyclerView.Adapter<OrderlinesAdapter.OrderlinesViewHolder>(){
    var orderlinesList:List<Orderline> = emptyList()
    var onListItemClick: OnListItemClick? = null

    fun setList(orderlinesList: List<Orderline>) {
        this.orderlinesList = orderlinesList
        notifyDataSetChanged()
    }

    inner class OrderlinesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemOrderlineImage: ImageView = itemView.findViewById(R.id.itemOrderlineImage)
        var itemOrderlinePlate: TextView = itemView.findViewById(R.id.itemOrderlinePlate)
        var itemOrderlineQuantity: TextView = itemView.findViewById(R.id.itemOrderlineQuantity)
        var itemOrderlinePrice: TextView = itemView.findViewById(R.id.itemOrderlinePrice)

        fun bind(orderline: Orderline){
            Glide.with(itemView.context).load(orderline.plateImage).into(itemOrderlineImage)

            itemOrderlinePlate.text = orderline.plateName
            itemOrderlineQuantity.text = orderline.quantity.toString()
            itemOrderlinePrice.text = orderline.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderlinesViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_orderline, parent, false)
        return OrderlinesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderlinesList.size
    }

    override fun onBindViewHolder(holder: OrderlinesViewHolder, position: Int) {
        var orderline: Orderline = orderlinesList.get(position)
        holder.bind(orderline)
    }

}