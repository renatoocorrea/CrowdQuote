package com.example.crowdslang

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.quote_form.view.*
import com.example.crowdslang.QuoteWebClient.QuoteResponse as QuoteResponse

class MainActivity : AppCompatActivity() {

    private val quotes: MutableList<Quote> = mutableListOf()

    private var alreadyWon: MutableList<Int> = mutableListOf()

    private lateinit var recyclerView: RecyclerView

    //What the fuck is :, override and ?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QuoteWebClient().list {
            this@MainActivity.quotes.addAll(it)
            configureList()
        }

        fab_add_quote.setOnClickListener {
            AddQuoteDialog(this@MainActivity,
                window.decorView as ViewGroup)
                .show { quote ->
                    this@MainActivity.quotes.add(quote)
                    configureList()
                }
        }

        fab_lucky.setOnClickListener {
            luckySeven()
        }

        configureList()
    }

    private fun configureList() {

        recyclerView = crowd_quotes_rv
        recyclerView.adapter = QuotesListAdapter(quotes, this)

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager


    }

    private fun luckySeven() {
        val size = recyclerView.adapter?.itemCount
        val list = (0..size!!).filter { it % 2 == 0 }
        val random = list.random()

        if (alreadyWon.contains(random)) {

        } else {
            alreadyWon.add(random)
            val view: View? = recyclerView.layoutManager?.findViewByPosition(random)
            view?.setBackgroundColor(Color.parseColor("#FFD700"))
        }
    }

    class AddQuoteDialog(private val context: Context,
                         private val viewGroup: ViewGroup) {


        fun show(created: (createdQuote: Quote) -> Unit) {
            val createdView = LayoutInflater.from(context)
                .inflate(R.layout.quote_form,
                    viewGroup,
                    false)

            AlertDialog.Builder(context)
                .setTitle("Add Quote")
                .setView(createdView)
                .setPositiveButton("Save") { _, _ ->
                    val author = createdView.form_quote_author.text.toString()
                    val quoteMessage = createdView.form_quote_message.text.toString()
                    val quote = Quote(author, quoteMessage)
                        QuoteWebClient().insert(quote, object : QuoteResponse<Quote> {
                            override fun success(quote: Quote) {
                                created(quote)
                            }
                        })
                    }
                .show()
        }

    }

}
