package tn.esprit.hollyfood.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.model.entities.Plate

class MenuAdapter(private val userId: String) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){
    var platesList:List<Plate> = emptyList()
    var onListItemClick: OnListItemClick? = null

    fun setList(platesList: List<Plate>) {
        this.platesList = platesList
        notifyDataSetChanged()
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var plateImage: ImageView = itemView.findViewById(R.id.plateImage)
        var plateName: TextView = itemView.findViewById(R.id.plateName)
        var platePrice: TextView = itemView.findViewById(R.id.platePrice)

        var btnAddPlatetoCart: TextView = itemView.findViewById(R.id.btnAddPlatetoCart)
        var btnEditPlate: TextView = itemView.findViewById(R.id.btnEditPlate)
        var btnDeletePlate: TextView = itemView.findViewById(R.id.btnDeletePlate)

        fun bind(plate: Plate){
            if(plate.userId != userId){
                btnEditPlate.visibility = View.GONE
                btnDeletePlate.visibility = View.GONE
            }

            Glide.with(itemView.context).load(plate.image).into(plateImage)

            plateName.text = plate.name
            platePrice.text = plate.price.toString()

            btnAddPlatetoCart.setOnClickListener{
                onListItemClick?.onAddToCartClick(plate)
            }

            btnEditPlate.setOnClickListener{
                onListItemClick?.onEditPlate(plate)
            }

            btnDeletePlate.setOnClickListener{
                onListItemClick?.onDeletePlate(plate)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_plate, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return platesList.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        var plate: Plate = platesList.get(position)
        holder.bind(plate)
    }

}