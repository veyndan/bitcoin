package com.veyndan.bitcoin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.widget.checkedChanges
import com.veyndan.bitcoin.data.Datapoint
import com.veyndan.bitcoin.data.Timespan
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

        timespan1Year.isChecked = true

        disposables += timespans.checkedChanges()
            .map { checkedId -> TIMESPAN_ID_TO_TIMESPAN.getValue(checkedId) }
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

    companion object {

        private val TIMESPAN_ID_TO_TIMESPAN = mapOf(
            R.id.timespan1Month to Timespan.DAYS_30,
            R.id.timespan2Months to Timespan.DAYS_60,
            R.id.timespan6Months to Timespan.DAYS_180,
            R.id.timespan1Year to Timespan.YEARS_1,
            R.id.timespan2Years to Timespan.YEARS_2,
            R.id.timespanAll to Timespan.ALL
        )
    }
}
