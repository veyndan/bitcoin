package com.veyndan.bitcoin.view

import com.robinhood.spark.SparkAdapter

class BitcoinSparkAdapter : SparkAdapter() {

    var datapoints = emptyList<com.veyndan.bitcoin.data.Datapoint>()

    override fun getX(index: Int): Float {
        return datapoints[index].timestamp.millis.toFloat()
    }

    override fun getY(index: Int): Float {
        return datapoints[index].price.value.toFloat()
    }

    override fun getItem(index: Int): Any {
        return datapoints[index]
    }

    override fun getCount(): Int {
        return datapoints.size
    }
}
