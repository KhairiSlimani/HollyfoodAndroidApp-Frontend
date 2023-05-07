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

class BillingAdapter : RecyclerView.Adapter<BillingAdapter.BillingViewHolder>(){
    var orderlinesList:List<Orderline> = emptyList()
    var onListItemClick: OnListItemClick? = null

    fun setList(orderlinesList: List<Orderline>) {
        this.orderlinesList = orderlinesList
        notifyDataSetChanged()
    }

    inner class BillingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemBillImage: ImageView = itemView.findViewById(R.id.itemBillImage)
        var itemBillName: TextView = itemView.findViewById(R.id.itemBillName)
        var itemBillCategory: TextView = itemView.findViewById(R.id.itemBillCategory)
        var itemBillPrice: TextView = itemView.findViewById(R.id.itemBillPrice)
        var itemBillQuantity: TextView = itemView.findViewById(R.id.itemBillQuantity)

        fun bind(orderline: Orderline){
            Glide.with(itemView.context).load(orderline.plateImage).into(itemBillImage)

            itemBillName.text = orderline.plateName
            itemBillCategory.text = orderline.plateCategory
            itemBillPrice.text = orderline.price.toString()
            itemBillQuantity.text = orderline.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_billing_plate, parent, false)
        return BillingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderlinesList.size
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        var orderline: Orderline = orderlinesList.get(position)
        holder.bind(orderline)
    }
}