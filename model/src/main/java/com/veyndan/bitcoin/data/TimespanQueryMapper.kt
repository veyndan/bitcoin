package com.veyndan.bitcoin.data

import io.reactivex.functions.Function

internal object TimespanQueryMapper : Function<Timespan, TimespanQuery> {

    override fun apply(timespanQuery: Timespan): TimespanQuery {
        return when (timespanQuery) {
            Timespan.DAYS_30 -> TimespanQuery.DAYS_30
            Timespan.DAYS_60 -> TimespanQuery.DAYS_60
            Timespan.DAYS_180 -> TimespanQuery.DAYS_180
            Timespan.YEARS_1 -> TimespanQuery.YEARS_1
            Timespan.YEARS_2 -> TimespanQuery.YEARS_2
            Timespan.ALL -> TimespanQuery.ALL
        }
    }
}
