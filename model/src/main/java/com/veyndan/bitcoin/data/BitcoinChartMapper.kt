package com.veyndan.bitcoin.data

import io.reactivex.functions.Function
import org.joda.time.Instant
import java.math.RoundingMode
import java.util.*

internal object BitcoinChartMapper : Function<BitcoinChartRaw, BitcoinChart> {

    override fun apply(bitcoinChartRaw: BitcoinChartRaw): BitcoinChart {
        return BitcoinChart(
            bitcoinChartRaw.name,
            bitcoinChartRaw.values.map { datapointRaw ->
                Datapoint(
                    timestamp = Instant.ofEpochSecond(datapointRaw.x),
                    price = Money(
                        datapointRaw.y.setScale(2, RoundingMode.HALF_UP),
                        Currency.getInstance("USD")
                    )
                )
            }
        )
    }
}
