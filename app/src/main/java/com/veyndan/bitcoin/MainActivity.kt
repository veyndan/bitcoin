package com.veyndan.bitcoin

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.veyndan.bitcoin.data.BitcoinChartService
import com.veyndan.bitcoin.rxbinding.checkedChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.blockchain.info/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val bitcoinChartService = retrofit.create<BitcoinChartService>()

        val bitcoinSparkAdapter = BitcoinSparkAdapter()

        sparkView.adapter = bitcoinSparkAdapter

        val chipIdToTimespan = mapOf(
            R.id.timespan30Days to "30days",
            R.id.timespan60Days to "60days",
            R.id.timespan180Days to "180days",
            R.id.timespan1Year to "1year",
            R.id.timespan2Years to "2years",
            R.id.timespanAll to "all"
        )

        disposables += timespans.checkedChanges()
            .flatMapSingle { checkedId ->
                bitcoinChartService.marketPrice(timespan = chipIdToTimespan[checkedId])
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                bitcoinSparkAdapter.datapoints = response.body()!!.values
                bitcoinSparkAdapter.notifyDataSetChanged()
            }

        timespans.check(R.id.timespan1Year)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
