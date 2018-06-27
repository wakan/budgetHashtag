package fr.budgethashtag.adapter

import android.content.ContentValues
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.budgethashtag.R
import fr.budgethashtag.contentprovider.BudgetProvider.Budget.KEY_COL_LIB
import fr.budgethashtag.contentprovider.BudgetProvider.Budget.KEY_COL_PREVISIONNEL


class MyBudgetViewHolder : RecyclerView.ViewHolder {

    private var tvLibelle: TextView
    private var tvMontant: TextView
    private var imageView: ImageView

    //itemView est la vue correspondante à 1 cellule
    constructor(itemView: View) : super(itemView) {
        //c'est ici que l'on fait nos findView
        tvLibelle = itemView.findViewById(R.id.tvLibelle)
        tvMontant = itemView.findViewById(R.id.tvMontant)
        imageView = itemView.findViewById(R.id.image)
    }


    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    fun bind(myObject: ContentValues) {
        tvLibelle.text = myObject.get(KEY_COL_LIB) as? String
        tvMontant.text = myObject.get(KEY_COL_PREVISIONNEL).toString()
        imageView.setImageResource(R.mipmap.ic_launcher_round)
       // Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView)
    }
}