package com.veyndan.bitcoin.data

import io.reactivex.functions.Function
import java.math.RoundingMode
import java.util.*
import kotlin.time.seconds

internal object BitcoinChartMapper : Function<BitcoinChartRaw, BitcoinChart> {

    override fun apply(bitcoinChartRaw: BitcoinChartRaw): BitcoinChart {
        return BitcoinChart(
            bitcoinChartRaw.name,
            bitcoinChartRaw.values.map { datapointRaw ->
                Datapoint(
                    timestamp = Date(datapointRaw.x.seconds.toLongMilliseconds()),
                    money = Money(
                        datapointRaw.y.setScale(2, RoundingMode.HALF_UP),
                        Currency.getInstance("USD")
                    )
                )
            }
        )
    }
}
