package com.veyndan.bitcoin.data

import com.squareup.moshi.Json

internal enum class TimespanQuery {
    @Json(name = "30days")
    DAYS_30,
    @Json(name = "60days")
    DAYS_60,
    @Json(name = "180days")
    DAYS_180,
    @Json(name = "1year")
    YEARS_1,
    @Json(name = "2years")
    YEARS_2,
    @Json(name = "all")
    ALL
}
