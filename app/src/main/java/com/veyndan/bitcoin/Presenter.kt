package com.veyndan.bitcoin

import com.veyndan.bitcoin.data.Datapoint
import com.veyndan.bitcoin.data.Timespan

interface Presenter {

    fun fetchChart(timespan: Timespan)

    fun fetchScrubInformation(datapoint: Datapoint)

    fun onViewDetached()
}
