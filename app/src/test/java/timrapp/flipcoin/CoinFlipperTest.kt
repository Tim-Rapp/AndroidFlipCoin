package timrapp.flipcoin

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class CoinFlipperTest {
    @Test
    fun flipCoin_EDGE() {
        val mockIntRange = mock(IntRange::class.java)
        given(mockIntRange.random()).willReturn(0)

        val coinFlipper = CoinFlipper(mockIntRange)

        assertEquals(coinFlipper.flipCoin(), FlipResult.EDGE)
    }

    @Test
    fun flipCoin_HEADS() {
        val mockIntRange = mock(IntRange::class.java)
        given(mockIntRange.random()).willReturn(3000)

        val coinFlipper = CoinFlipper(mockIntRange)

        assertEquals(coinFlipper.flipCoin(), FlipResult.HEADS)
    }

    @Test
    fun flipCoin_TAILS() {
        val mockIntRange = mock(IntRange::class.java)
        given(mockIntRange.random()).willReturn(6000)

        val coinFlipper = CoinFlipper(mockIntRange)

        assertEquals(coinFlipper.flipCoin(), FlipResult.TAILS)
    }
}
