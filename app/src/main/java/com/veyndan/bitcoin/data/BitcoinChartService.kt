package com.veyndan.bitcoin.data

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BitcoinChartService {

    @GET("charts/market-price")
    fun marketPrice(
        @Query("timespan") timespan: String? = null,
        @Query("rollingAverage") rollingAverage: String? = null,
        @Query("start") start: String? = null
    ): Single<Response<BitcoinChart>>
}
