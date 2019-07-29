package com.example.crowdslang

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.quote_item.view.*

class QuotesListAdapter(private val quotes: List<Quote>, private val context: Context) : Adapter<QuotesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quote_item, parent, false)
        return ViewHolder(view)
    }



    override fun getItemCount(): Int {
        return quotes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]

        holder?.let {
            it.bindView(quote)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(quoteObject: Quote) {
            val whoSaid = itemView.who_said
            val quote = itemView.quote

            whoSaid.text = quoteObject.whoSaid
            quote.text = quoteObject.quote
        }
    }
}

