package com.veyndan.bitcoin.data

import io.reactivex.functions.Function

internal object TimespanQueryMapper : Function<Timespan, String> {

    override fun apply(timespanQuery: Timespan): String {
        return when (timespanQuery) {
            Timespan.MONTHS_1 -> "30days"
            Timespan.MONTHS_2 -> "60days"
            Timespan.MONTHS_6 -> "180days"
            Timespan.YEARS_1 -> "1year"
            Timespan.YEARS_2 -> "2years"
            Timespan.ALL -> "all"
        }
    }
}
