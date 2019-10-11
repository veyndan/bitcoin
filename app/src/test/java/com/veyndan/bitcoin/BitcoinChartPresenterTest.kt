package com.veyndan.bitcoin

import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.veyndan.bitcoin.data.Datapoint
import com.veyndan.bitcoin.data.Money
import com.veyndan.bitcoin.data.Timespan
import com.veyndan.bitcoin.presenter.BitcoinChartPresenter
import com.veyndan.bitcoin.presenter.ScrubInformation
import com.veyndan.bitcoin.view.View
import org.joda.time.Instant
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

// TODO Instrumentation runner not found!

@RunWith(MockitoJUnitRunner::class)
class BitcoinChartPresenterTest {

    // TODO Does primaryLocale need to be mocked?
    private val applicationContext = InstrumentationRegistry.getInstrumentation().context

    private val view = mock<View>()
    private val presenter = BitcoinChartPresenter(applicationContext, view)

    @Test
    fun `Display scrub information with correct value`() {
        presenter.fetchScrubInformation(
            Datapoint(
                Instant.ofEpochSecond(1568246400),
                Money(243.12.toBigDecimal(), Currency.getInstance("USD"))
            )
        )

        verify(view).displayScrubInformation(ScrubInformation("243.12 $", "12/09/2019"))
    }

    @Test
    fun `Display chart correctly`() {
        // TODO Mock retrofit using dagger
        presenter.fetchChart(Timespan.YEARS_2)

        verify(view).updateChartName("Market Price (USD)")
        verify(view).updateChart(any())
    }
}
