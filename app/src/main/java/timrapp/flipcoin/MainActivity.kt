package timrapp.flipcoin

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()

        val startFlippingButton: Button = findViewById(R.id.startFlippingButton)
        startFlippingButton.setOnClickListener {
            disableButton(startFlippingButton)

            try {
                val timeToFlipString = findViewById<EditText>(R.id.timeToFlip).text.toString()
                val delayBetweenFlips = timeToFlipString.toFloatOrNull() ?: 0.0F

                if (delayBetweenFlips.compareTo(0.0) == 0) {
                    Toast.makeText(this, "Please enter a non-zero value", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                launch {
                    flipCoinUntilSide(delayBetweenFlips)
                }
            } finally {
                enableButton(startFlippingButton)
            }
        }
    }

    private suspend fun flipCoinUntilSide(delayBetweenFlips: Float) {
        withContext(Dispatchers.Default) {
            var count = 0
            while (true) {
                count++

                val flipResult = CoinFlipper().flipCoin()
                if (flipResult == FlipResult.SIDE) {
                    withContext(Dispatchers.Main) {
                        displayFlipResult(Pair(flipResult, count))
                    }
                    break
                } else {
                    if (count > 0 && count % 1000 == 0) {
                        withContext(Dispatchers.Main) {
                            displayFlipResult(Pair(flipResult, count))
                        }
                    }
                    delay(delayBetweenFlips.toLong())
                }
            }
        }
    }

    private suspend fun displayFlipResult(resultPair: Pair<FlipResult, Int>) {
        val flipResult = resultPair.first
        val numFlips = resultPair.second

        when (flipResult) {
            FlipResult.SIDE -> {
                Toast.makeText(this, "Nickel landed on its side after $numFlips flips", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Completed $numFlips flips", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableButton(button: Button) {
        button.setBackgroundColor(Color.WHITE)
        button.isEnabled = true
    }

    private fun disableButton(button: Button) {
        button.isEnabled = false
        button.setBackgroundColor(Color.DKGRAY)
    }
}

