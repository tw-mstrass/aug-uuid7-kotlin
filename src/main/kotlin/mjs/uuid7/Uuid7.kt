package mjs.uuid7

import java.util.UUID
import kotlin.random.Random
import java.time.Instant

object Uuid7 {
    fun generate(): UUID {
        // Get current timestamp in milliseconds
        val timestamp = Instant.now().toEpochMilli()

        // Create the most significant bits:
        // - 48 bits: timestamp
        // - 4 bits: version (7)
        // - 12 bits: random
        val msb = (timestamp shl 16) or // Shift timestamp to the left by 16 bits
                 (7L shl 12) or // Set version to 7 (bits 12-15)
                 (Random.nextInt(0, 4096).toLong()) // 12 random bits

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
