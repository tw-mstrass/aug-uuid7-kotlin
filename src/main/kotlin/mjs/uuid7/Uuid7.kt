package mjs.uuid7

import java.util.UUID
import kotlin.random.Random
import java.time.Instant

object Uuid7 {
    fun generate(): UUID {
        // Get current timestamp in milliseconds
        val timestamp = Instant.now().toEpochMilli()

        // Generate random bits for the UUID
        val randomMsb = Random.nextLong()
        val randomLsb = Random.nextLong()

        // Set the timestamp in the most significant 48 bits
        // Clear the most significant 48 bits and then set them to the timestamp
        val msb = ((timestamp and 0x0000_FFFF_FFFF_FFFFL) shl 16) or
                 (randomMsb and 0x0000_0000_0000_FFFFL)

        // Set the version bits to 7
        // Clear the version bits (bits 12-15) and then set them to 7
        val msbWithVersion = (msb and -0x1000) or 0x7000

        // Set the variant bits to 10 (binary) or 2 (decimal)
        // Clear the variant bits (bits 62-63) and then set them to 10 (binary)
        // We use Long.MIN_VALUE which is 0x8000000000000000L (bit 63 set to 1)
        val lsb = (randomLsb and Long.MAX_VALUE) or Long.MIN_VALUE

        return UUID(msbWithVersion, lsb)
    }

    fun extractTimestamp(uuid: UUID): Long {
        // Extract the timestamp from the most significant 48 bits
        return uuid.mostSignificantBits ushr 16
    }
}
