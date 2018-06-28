package fr.budgethashtag.adapter

import android.content.ContentValues
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.budgethashtag.R

class MyTransactionAdapter() : RecyclerView.Adapter<MyTransactionViewHolder>() {

    private lateinit var listTransactions:  List<ContentValues>

    constructor(list: List<ContentValues>) : this(){
        this.listTransactions = list
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTransactionViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.cell_cards, parent, false)
        return MyTransactionViewHolder(view)
    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque MyObjects
    override fun onBindViewHolder(holder: MyTransactionViewHolder, position: Int) {
        val myObject = listTransactions[position]
        holder.bind(myObject)
    }

    override fun getItemCount(): Int {
        return  listTransactions.size
    }
}