package com.veyndan.bitcoin.data

import io.reactivex.functions.Function

internal object TimespanQueryMapper : Function<Timespan, String> {

    override fun apply(timespanQuery: Timespan): String {
        return when (timespanQuery) {
            Timespan.DAYS_30 -> "30days"
            Timespan.DAYS_60 -> "60days"
            Timespan.DAYS_180 -> "180days"
            Timespan.YEARS_1 -> "1year"
            Timespan.YEARS_2 -> "2years"
            Timespan.ALL -> "all"
        }
    }
}
