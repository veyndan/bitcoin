package com.veyndan.bitcoin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.widget.checkedChanges
import com.veyndan.bitcoin.data.Datapoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View {

    private lateinit var presenter: Presenter

    private val bitcoinSparkAdapter = BitcoinSparkAdapter()

    override fun updateChartName(chartName: String) {
        chartTitle.text = chartName
    }

    override fun updateChart(datapoints: List<Datapoint>) {
        bitcoinSparkAdapter.datapoints = datapoints
        bitcoinSparkAdapter.notifyDataSetChanged()
    }

    override fun displayScrubInformation(scrubInformation: ScrubInformation) {
        scrubPrice.text = scrubInformation.price
        scrubDate.text = scrubInformation.date
    }

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = BitcoinChartPresenter(applicationContext, this)

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
            .map { checkedId -> chipIdToTimespan.getValue(checkedId) }
            .subscribe { timespan -> presenter.fetchChart(timespan) }

        sparkView.setScrubListener {
            val datapoint = it as Datapoint?

            // When no longer scrubbing, null is passed
            if (datapoint != null) {
                if (!scrubInformation.isShown) {
                    information.showNext()
                }

                presenter.fetchScrubInformation(datapoint)
            } else {
                if (!chartTitle.isShown) {
                    information.showNext()
                }
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
        presenter.onViewDetached()
        super.onDestroy()
    }
}
