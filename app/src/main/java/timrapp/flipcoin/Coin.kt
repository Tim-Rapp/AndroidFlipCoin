package timrapp.flipcoin


enum class FlipResult {
    HEADS, TAILS, EDGE
}

class CoinFlipper(private val intRange: IntRange) {
    fun flipCoin(): FlipResult {
        val rand = intRange.random()
        return when {
            rand == 0 -> {
                FlipResult.EDGE
            }
            rand <= 3000 -> {
                FlipResult.HEADS
            }
            else -> {
                FlipResult.TAILS
            }
        }
    }
}

