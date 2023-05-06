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
import tn.esprit.hollyfood.viewmodel.OrderlineViewModel

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){
    var orderlinesList:List<Orderline> = emptyList()
    var onListItemClick: OnListItemClick? = null

    fun setList(orderlinesList: List<Orderline>) {
        this.orderlinesList = orderlinesList
        notifyDataSetChanged()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemCartImage: ImageView = itemView.findViewById(R.id.itemCartImage)
        var itemCartName: TextView = itemView.findViewById(R.id.itemCartName)
        var itemCartCategory: TextView = itemView.findViewById(R.id.itemCartCategory)
        var itemCartPrice: TextView = itemView.findViewById(R.id.itemCartPrice)
        var itemCartQuantity: TextView = itemView.findViewById(R.id.itemCartQuantity)
        var imagePlus: ImageView = itemView.findViewById(R.id.imagePlus)
        var imageMinus: ImageView = itemView.findViewById(R.id.imageMinus)


        fun bind(orderline: Orderline){
            Glide.with(itemView.context).load(orderline.plateImage).into(itemCartImage)

            itemCartName.text = orderline.plateName
            itemCartCategory.text = orderline.plateCategory
            itemCartPrice.text = orderline.price.toString()
            itemCartQuantity.text = orderline.quantity.toString()

            imagePlus.setOnClickListener{
                orderline.quantity++
                orderline.price += orderline.unitPrice

                itemCartPrice.text = orderline.price.toString()
                itemCartQuantity.text = orderline.quantity.toString()

                OrderlineViewModel.calculateTotal()
            }

            imageMinus.setOnClickListener{
                if(orderline.quantity > 1){
                    orderline.quantity--
                    orderline.price -= orderline.unitPrice

                    itemCartPrice.text = orderline.price.toString()
                    itemCartQuantity.text = orderline.quantity.toString()

                    OrderlineViewModel.calculateTotal()
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderlinesList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var orderline: Orderline = orderlinesList.get(position)
        holder.bind(orderline)
    }

}