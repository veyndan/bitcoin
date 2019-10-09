package com.veyndan.bitcoin.data

data class BitcoinChart(
    val name: String,
    val values: List<Datapoint>
)

data class Datapoint(
    val x: Long,
    val y: Double
)
