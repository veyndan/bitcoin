package com.veyndan.bitcoin

import com.veyndan.bitcoin.data.Datapoint

interface View {

    fun updateChartName(chartName: String)

    fun updateChart(datapoints: List<Datapoint>)

    fun displayScrubInformation(scrubInformation: ScrubInformation)
}
