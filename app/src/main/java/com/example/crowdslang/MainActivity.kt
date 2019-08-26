package com.example.crowdslang

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.crowdslang.QuoteWebClient.QuoteResponse
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.quote_form.view.*

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
        val layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        recyclerView.layoutManager = layoutManager
    }

    private fun luckySeven() {
        val size = recyclerView.adapter?.itemCount?.minus(1)
        val list = (1..size!!).filter { !alreadyWon.contains(it) }
        val random = list.random()
        if (alreadyWon.size != 5) {
                val manager = recyclerView.layoutManager as LinearLayoutManager
                if (random >= manager.findFirstCompletelyVisibleItemPosition() && random <= manager.findLastCompletelyVisibleItemPosition()){
                    changingWinnerColor(random)
                } else {
                    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
                            if (state == RecyclerView.SCROLL_STATE_IDLE) {
                                changingWinnerColor(random)
                                recyclerView.removeOnScrollListener(this)
                            }
                        }
                    })
                    recyclerView.smoothScrollToPosition(random)
                }
        } else {
            Toasty.warning(this, "We are out of stickers! ", Toast.LENGTH_LONG, true).show()
        }

    }

    private fun changingWinnerColor(position : Int){
        alreadyWon.add(position)
        val view = recyclerView.layoutManager?.findViewByPosition(position)
            ?.findViewById<TextView>(R.id.who_said)
        view?.setBackgroundColor(Color.parseColor("#FFD700"))
        Toasty.success(this@MainActivity, "I have Won! " + view?.text.toString(), Toast.LENGTH_SHORT, true).show()
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
