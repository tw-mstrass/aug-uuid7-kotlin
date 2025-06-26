package mjs.uuid7

import java.util.UUID
import kotlin.random.Random
import java.time.Instant
import java.util.concurrent.atomic.AtomicLong

object Uuid7 {
    // Keep track of the last timestamp and a counter for UUIDs generated within the same millisecond
    private var lastTimestamp = AtomicLong(0)
    private var counter = AtomicLong(0)

    fun generate(): UUID {
        // Get current timestamp in milliseconds
        val timestamp = Instant.now().toEpochMilli()

        // Ensure monotonically increasing timestamps and manage the counter
        val counterValue = synchronized(this) {
            val last = lastTimestamp.get()
            if (timestamp <= last) {
                // If the current timestamp is less than or equal to the last timestamp,
                // use the last timestamp and increment the counter
                counter.incrementAndGet()
            } else {
                // If the current timestamp is greater than the last timestamp,
                // update the last timestamp and reset the counter
                lastTimestamp.set(timestamp)
                counter.set(0)
                0L
            }
        }

        // Create the most significant bits:
        // - 48 bits: timestamp
        // - 4 bits: version (7)
        // - 12 bits: counter (instead of random)
        val msb = (timestamp shl 16) or // Shift timestamp to the left by 16 bits
                 (7L shl 12) or // Set version to 7 (bits 12-15)
                 (counterValue and 0xFFFL) // 12 bits for counter (0-4095)

        // Create the least significant bits:
        // - 2 bits: variant (10 binary)
        // - 62 bits: random
        val lsb = (2L shl 62) or // Set variant to 10 binary (bits 62-63)
                 (Random.nextLong() and 0x3FFFFFFFFFFFFFFFL) // 62 random bits

        return UUID(msb, lsb)
    }

    fun extractTimestamp(uuid: UUID): Long {
        // Extract the timestamp from the most significant 48 bits
        return uuid.mostSignificantBits ushr 16
    }
}
