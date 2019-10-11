package com.veyndan.bitcoin.view

import com.veyndan.bitcoin.data.Datapoint
import com.veyndan.bitcoin.presenter.ScrubInformation

interface View {

    fun updateChartName(chartName: String)

    fun updateChart(datapoints: List<Datapoint>)

    fun displayScrubInformation(scrubInformation: ScrubInformation)
}
