package com.veyndan.bitcoin.data

import org.joda.time.Instant
import java.math.BigDecimal
import java.util.*

data class BitcoinChart(
    val name: String,
    val datapoints: List<Datapoint>
)

data class Datapoint(
    val timestamp: Instant,
    val price: Money
)

data class Money(
    val value: BigDecimal,
    val currency: Currency
)
