package fr.budgethashtag.adapter

import android.content.ContentValues
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.budgethashtag.R
import fr.budgethashtag.basecolumns.Transaction.*

class MyTransactionViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var tvLibelle: TextView = itemView.findViewById(R.id.tvLibelle)
    private var tvMontant: TextView = itemView.findViewById(R.id.tvMontant)
    private var imageView: ImageView = itemView.findViewById(R.id.image)


    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    fun bind(myObject: ContentValues) {
        tvLibelle.text = myObject.get(KEY_COL_LIB) as? String
        tvMontant.text = myObject.get(KEY_COL_MONTANT).toString()
        imageView.setImageResource(R.mipmap.ic_launcher)
        // Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView)
    }
}