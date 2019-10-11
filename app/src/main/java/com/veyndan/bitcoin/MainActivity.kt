package com.veyndan.bitcoin

import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import com.jakewharton.rxbinding3.widget.checkedChanges
import com.veyndan.bitcoin.data.BitcoinRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        timespan1Year.isChecked = true

        disposables += timespans.checkedChanges()
            .flatMapCompletable { checkedId ->
                val timespan = chipIdToTimespan.getValue(checkedId)
                BitcoinRepository.fetchBitcoinMarketPriceChart(timespan)
            }
            .subscribe()

        disposables += BitcoinRepository.getAllBitcoinMarketPriceChart()
            .subscribe { bitcoinCharts ->
                val timespan = chipIdToTimespan.getValue(timespans.checkedRadioButtonId)
                val bitcoinChart = bitcoinCharts.getValue(timespan)

                bitcoinSparkAdapter.datapoints = bitcoinChart.datapoints
                bitcoinSparkAdapter.notifyDataSetChanged()

                chartTitle.text = bitcoinChart.name
            }

        sparkView.setScrubListener {
            // When no longer scrubbing, null is passed
            if (it != null) {
                if (!scrubInformation.isShown) {
                    information.showNext()
                }

                val datapoint = it as com.veyndan.bitcoin.data.Datapoint

                val primaryLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
                val format = NumberFormat.getCurrencyInstance(primaryLocale).apply {
                    currency = datapoint.money.currency
                }
                val price = format.format(datapoint.money.value)
                scrubValue.text = price

                val date = DateFormat.getDateFormat(applicationContext).format(datapoint.timestamp)
                scrubTime.text = date
            } else {
                if (!chartTitle.isShown) {
                    information.showNext()
                }
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
