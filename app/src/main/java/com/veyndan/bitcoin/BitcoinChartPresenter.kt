package com.veyndan.bitcoin

import android.content.Context
import android.text.format.DateFormat
import androidx.core.os.ConfigurationCompat
import com.veyndan.bitcoin.data.BitcoinRepository
import com.veyndan.bitcoin.data.Datapoint
import com.veyndan.bitcoin.data.Timespan
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.text.NumberFormat

class BitcoinChartPresenter(
    private val applicationContext: Context,
    private val view: View
) : Presenter {

    private val primaryLocale = run {
        val configuration = applicationContext.resources.configuration
        ConfigurationCompat.getLocales(configuration)[0]
    }
    private val currencyFormat = NumberFormat.getCurrencyInstance(primaryLocale)

    private val disposables = CompositeDisposable()

    override fun fetchChart(timespan: Timespan) {
        disposables += BitcoinRepository.fetchBitcoinMarketPriceChart(timespan)
            .andThen(BitcoinRepository.getAllBitcoinMarketPriceChart())
            .map { bitcoinCharts -> bitcoinCharts.getValue(timespan) }
            .firstOrError()
            .subscribe { bitcoinChart ->
                view.updateChartName(bitcoinChart.name)
                view.updateChart(bitcoinChart.datapoints)
            }
    }

    override fun fetchScrubInformation(datapoint: Datapoint) {
        val format = currencyFormat.apply { currency = datapoint.price.currency }
        val price = format.format(datapoint.price.value)

        val date = DateFormat.getDateFormat(applicationContext).format(datapoint.timestamp)

        view.displayScrubInformation(ScrubInformation(price, date))
    }

    override fun onViewDetached() {
        disposables.clear()
    }
}
