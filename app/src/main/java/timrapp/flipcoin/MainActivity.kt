package timrapp.flipcoin

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job

    private val toasts = mutableListOf<Toast>()

    // every flip has a 1 in 6000 chance of landing on its edge
    private val expectedFlips = 6000

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()
        val startFlippingButton: Button = findViewById(R.id.startFlippingButton)

        startFlippingButton.setOnClickListener {
            disableButton(startFlippingButton)
            val resultTextView = findViewById<TextView>(R.id.result)
            resultTextView.text = ""

            try {
                val timeToFlipString = findViewById<EditText>(R.id.timeToFlip).text.toString()
                val delayBetweenFlips = timeToFlipString.toFloatOrNull() ?: 0.0F

                if (delayBetweenFlips.compareTo(0.0) == 0) {
                    Toast.makeText(this, "Please enter a non-zero value", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (delayBetweenFlips > 100) {
                    val expectedSeconds = (delayBetweenFlips / 1000 * expectedFlips).toInt()
                    Toast.makeText(
                        this,
                        "It would take about $expectedSeconds seconds to finish with such a long pause, please pick a smaller number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                launch {
                    flipCoinUntilOnEdge(delayBetweenFlips)
                }
            } finally {
                enableButton(startFlippingButton)
            }
        }
    }

    private suspend fun flipCoinUntilOnEdge(delayBetweenFlips: Float) {
        withContext(Dispatchers.Default) {
            var count = 0
            while (true) {
                count++

                val flipResult = CoinFlipper((0..expectedFlips)).flipCoin()
                if (flipResult == FlipResult.EDGE) {
                    withContext(Dispatchers.Main) {
                        displayFlipResult(Pair(flipResult, count))
                    }
                    break
                } else {
                    if (isLongDelay(delayBetweenFlips) && count > 0 && count % 1000 == 0) {
                        withContext(Dispatchers.Main) {
                            displayProgress(Pair(flipResult, count))
                        }
                    }
                    delay(delayBetweenFlips.toLong())
                }
            }
        }
    }

    private fun isLongDelay(delayBetweenFlips: Float): Boolean {
        return delayBetweenFlips > 1
    }

    private fun displayFlipResult(resultPair: Pair<FlipResult, Int>) {
        // if toasts are queued up to display they can appear after we have already displayed the result
        toasts.map { toast ->
            toast.cancel()
        }

        val numFlips = resultPair.second
        val resultTextView = findViewById<TextView>(R.id.result)
        resultTextView.text = String.format(resources.getString(R.string.result), numFlips)
    }

    private fun displayProgress(resultPair: Pair<FlipResult, Int>) {
        val numFlips = resultPair.second
        val toast = Toast.makeText(this, "Completed $numFlips flips", Toast.LENGTH_SHORT)
        toasts.add(toast)
        toast.show()
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

