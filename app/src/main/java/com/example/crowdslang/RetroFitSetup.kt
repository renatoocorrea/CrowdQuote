package com.example.crowdslang

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

class RetroFitSetup {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://105.103.87.142:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    interface QuotesService{
        @GET("quotes")
        fun list(): Call<List<Quote>>

        @POST("quotes")
        fun insert(@Body quote: Quote): Call<Quote>
    }

    fun quoteService() = retrofit.create(QuotesService::class.java)
}