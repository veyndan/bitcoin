package com.veyndan.bitcoin.data

import java.math.BigDecimal
import java.util.*

data class BitcoinChart(
    val name: String,
    val datapoints: List<Datapoint>
)

data class Datapoint(
    val timestamp: Date,
    val money: Money
)

data class Money(
    val value: BigDecimal,
    val currency: Currency
)
