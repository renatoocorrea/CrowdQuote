package com.example.crowdslang

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.crowdslang.QuoteWebClient.QuoteResponse as QuoteResponse

class QuoteWebClient {

    fun list(canBeAnyNameThatIWant: (notes: List<Quote>) -> Unit){
        val call = RetroFitSetup().quoteService().list()
        call.enqueue(object: Callback<List<Quote>?> {
            override fun onResponse(call: Call<List<Quote>?>?,
                                    response: Response<List<Quote>?>?) {
                response?.body()?.let {
                    val quotes: List<Quote> = it
                    canBeAnyNameThatIWant(quotes)
                }
            }

            override fun onFailure(call: Call<List<Quote>?>?,
                                   t: Throwable?) {
                Log.e("QUOTE", " Deu ruim na leitura da lista tiu ow. " + t?.message)
            }
        })
    }

    fun insert(quote: Quote, quoteResponse: QuoteResponse<Quote>){
        val call = RetroFitSetup().quoteService().insert(quote)
        call.enqueue(object: Callback<Quote?> {
            override fun onResponse(call: Call<Quote?>?, response: Response<Quote?>?) {
                quoteResponse.success(quote)
            }

            override fun onFailure(call: Call<Quote?>?, t: Throwable?) {
                Log.e("QUOTE", "Deu ruim a inserção do quote tiu ow. " + t?.message)
            }
        })
    }

    interface QuoteResponse<T> {
        fun success(quote: T)
    }

}