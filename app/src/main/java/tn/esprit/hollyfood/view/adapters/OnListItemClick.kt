package tn.esprit.hollyfood.view.adapters

import tn.esprit.hollyfood.model.entities.Restaurant

interface OnListItemClick {
    fun onItemClick(restaurant: Restaurant)
}