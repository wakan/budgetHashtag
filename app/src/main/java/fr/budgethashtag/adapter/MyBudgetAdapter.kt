package fr.budgethashtag.adapter

import android.content.ContentValues
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.budgethashtag.R


class MyBudgetAdapter() : RecyclerView.Adapter<MyBudgetViewHolder>() {

    private lateinit var listBudgets:  List<ContentValues>

    constructor(list: List<ContentValues>) : this(){
        this.listBudgets = list
    }

    //ajouter un constructeur prenant en entrée une list
   /* fun MyBudgetAdapter( list: List<ContentValues>) {
        this.listBudgets = list
    }*/

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBudgetViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.cell_cards, parent, false)
        return MyBudgetViewHolder(view)
    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque MyObjects
    override fun onBindViewHolder(holder: MyBudgetViewHolder, position: Int) {
        val myObject = listBudgets[position]
        holder.bind(myObject)
    }

    override fun getItemCount(): Int {
        return  listBudgets.size
    }
}