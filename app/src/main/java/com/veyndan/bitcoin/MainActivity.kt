package com.veyndan.bitcoin

import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.veyndan.bitcoin.data.BitcoinChartService
import com.veyndan.bitcoin.data.Datapoint
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
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import kotlin.time.seconds


object BigDecimalAdapter {

    @FromJson
    fun fromJson(string: String) = BigDecimal(string)

    @ToJson
    fun toJson(value: BigDecimal) = value.toString()
}

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val moshi = Moshi.Builder()
            .add(BigDecimalAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.blockchain.info/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val bitcoinChartService = retrofit.create<BitcoinChartService>()

        val bitcoinSparkAdapter = BitcoinSparkAdapter()

        sparkView.adapter = bitcoinSparkAdapter

        val chipIdToTimespan = mapOf(
            R.id.timespan1Month to "30days",
            R.id.timespan2Months to "60days",
            R.id.timespan6Months to "180days",
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

        sparkView.setScrubListener {
            // When no longer scrubbing, null is passed
            if (it != null) {
                val datapoint = it as Datapoint
                val primaryLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
                val format = NumberFormat.getCurrencyInstance(primaryLocale).apply {
                    currency = Currency.getInstance("USD")
                }
                val price = format.format(datapoint.y.setScale(2, RoundingMode.HALF_UP))
                scrubValue.text = price

                val date = DateFormat.getDateFormat(applicationContext).format(Date(datapoint.x.seconds.toLongMilliseconds()))
                scrubTime.text = date
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
