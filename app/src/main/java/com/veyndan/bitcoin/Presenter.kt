package com.veyndan.bitcoin

import com.veyndan.bitcoin.data.Datapoint

interface Presenter {

    fun fetchChart(timespan: String)

    fun fetchScrubInformation(datapoint: Datapoint)

    fun onViewDetached()
}
