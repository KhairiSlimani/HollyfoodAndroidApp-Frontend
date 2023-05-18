package tn.esprit.hollyfood.view.adapters

import tn.esprit.hollyfood.model.entities.Order
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant

interface OnListItemClick {
    fun onItemClick(restaurant: Restaurant)
    fun onAddToCartClick(plate: Plate)
    fun onDeleteOrder(order: Order)
    fun onEditPlate(plate: Plate)
    fun onDeletePlate(plate: Plate)
    fun onOrderClick(order: Order)
}