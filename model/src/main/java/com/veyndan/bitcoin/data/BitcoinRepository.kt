package com.veyndan.bitcoin.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.ConcurrentHashMap

object BitcoinRepository {

    private val store = ConcurrentHashMap<String, BitcoinChart>()

    private val subject = PublishSubject.create<Map<String, BitcoinChart>>()

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val moshi = Moshi.Builder()
        .add(BigDecimalAdapter)
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.blockchain.info/")
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val bitcoinChartService = retrofit.create<BitcoinChartService>()

    private fun memory(timespan: String): Maybe<BitcoinChart> =
        Maybe.fromCallable { store[timespan] }

    private fun network(timespan: String): Single<BitcoinChart> =
        bitcoinChartService.marketPrice(timespan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> response.body()!! } // TODO Elegantly handle error
            .map(BitcoinChartMapper)
            .doOnSuccess { bitcoinChart ->
                store[timespan] = bitcoinChart
                subject.onNext(store)
            }

    fun getAllBitcoinMarketPriceChart(): Observable<Map<String, BitcoinChart>> {
        return subject.share()
    }

    fun fetchBitcoinMarketPriceChart(timespan: String): Completable {
        return Maybe.concat(memory(timespan), network(timespan).toMaybe())
            .firstOrError()
            .doOnSuccess { subject.onNext(store) }
            .ignoreElement()
    }
}
