package com.veyndan.bitcoin

import com.robinhood.spark.SparkAdapter
import com.veyndan.bitcoin.data.Datapoint

class BitcoinSparkAdapter : SparkAdapter() {

    var datapoints = emptyList<com.veyndan.bitcoin.data.Datapoint>()

    override fun getX(index: Int): Float {
        return datapoints[index].timestamp.time.toFloat()
    }

    override fun getY(index: Int): Float {
        return datapoints[index].money.value.toFloat()
    }

    override fun getItem(index: Int): Any {
        return datapoints[index]
    }

    override fun getCount(): Int {
        return datapoints.size
    }
}
