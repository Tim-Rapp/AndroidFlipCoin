package timrapp.flipcoin


enum class FlipResult {
    HEADS, TAILS, SIDE
}

class CoinFlipper {
    fun flipCoin(): FlipResult {
        // takes about 6000 flips for a nickel to land on its side
        val rand = (0..6000).random()
        return when {
            rand == 0 -> {
                FlipResult.SIDE
            }
            rand < 3000 -> {
                FlipResult.HEADS
            }
            else -> {
                FlipResult.TAILS
            }
        }
    }
}

