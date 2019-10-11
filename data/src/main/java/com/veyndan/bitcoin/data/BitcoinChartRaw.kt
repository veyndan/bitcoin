package com.veyndan.bitcoin.data

import java.math.BigDecimal

internal data class BitcoinChartRaw(
    val name: String,
    val values: List<DatapointRaw>
)

internal data class DatapointRaw(
    val x: Long,
    val y: BigDecimal
)
