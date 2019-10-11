package com.veyndan.bitcoin.data

import org.joda.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class BitcoinChartMapperTest {

    @Test
    fun `Chart is mapped correctly`() {
        val bitcoinChartRaw = BitcoinChartRaw(
            "Market Price (USD)",
            listOf(
                DatapointRaw(1568160000, 10045.765000000001.toBigDecimal()),
                DatapointRaw(1568246400, 10209.944166666666.toBigDecimal()),
                DatapointRaw(1568332800, 10318.525833333333.toBigDecimal()),
                DatapointRaw(1568419200, 10338.066666666668.toBigDecimal()),
                DatapointRaw(1568505600, 10322.44.toBigDecimal()),
                DatapointRaw(1568592000, 10249.153333333334.toBigDecimal())
            )
        )

        val bitcoinChart = BitcoinChartMapper.apply(bitcoinChartRaw)

        assertEquals(
            BitcoinChart(
                "Market Price (USD)",
                listOf(
                    Datapoint(
                        Instant.ofEpochSecond(1568160000),
                        Money(10045.77.toBigDecimal(), Currency.getInstance("USD"))
                    ),
                    Datapoint(
                        Instant.ofEpochSecond(1568246400),
                        Money(10209.94.toBigDecimal(), Currency.getInstance("USD"))
                    ),
                    Datapoint(
                        Instant.ofEpochSecond(1568332800),
                        Money(10318.53.toBigDecimal(), Currency.getInstance("USD"))
                    ),
                    Datapoint(
                        Instant.ofEpochSecond(1568419200),
                        Money(10338.07.toBigDecimal(), Currency.getInstance("USD"))
                    ),
                    Datapoint(
                        Instant.ofEpochSecond(1568505600),
                        Money(10322.44.toBigDecimal(), Currency.getInstance("USD"))
                    ),
                    Datapoint(
                        Instant.ofEpochSecond(1568592000),
                        Money(10249.15.toBigDecimal(), Currency.getInstance("USD"))
                    )
                )
            ),
            bitcoinChart
        )
    }
}
