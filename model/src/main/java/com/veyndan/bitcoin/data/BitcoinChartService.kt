package com.veyndan.bitcoin.data

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface BitcoinChartService {

    @GET("charts/market-price")
    fun marketPrice(
        @Query("timespan") timespan: TimespanQuery? = null
    ): Single<Response<BitcoinChartRaw>>
}
