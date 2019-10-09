package com.veyndan.bitcoin

import com.robinhood.spark.SparkAdapter
import com.veyndan.bitcoin.data.Datapoint

class BitcoinSparkAdapter : SparkAdapter() {

    var datapoints = emptyList<Datapoint>()

    override fun getX(index: Int): Float {
        return datapoints[index].x.toFloat()
    }

    override fun getY(index: Int): Float {
        return datapoints[index].y.toFloat()
    }

    override fun getItem(index: Int): Any {
        return datapoints[index]
    }

    override fun getCount(): Int {
        return datapoints.size
    }
}
