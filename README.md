# AndroidFlipCoin
Flipping a coin results in "heads" or "tails". Except when it doesn't. A nickel will land on it's edge about every 6000 flips (source:  https://journals.aps.org/pre/abstract/10.1103/PhysRevE.48.2547). This app simulates flipping a nickel until it lands on it's edge, with a configurable delay between flips. Uses Kotlin coroutines for asynchronous processing.

## Enhancements
Possible future enhancements include testing coroutine logic, replacing coroutines with ReactiveX, adding other coin types (cent, dime, etc), and ensuring that app behaves when paused/stopped/restarted.