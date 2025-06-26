package mjs.uuid7

import java.util.UUID
import kotlin.random.Random

object Uuid7 {
    fun generate(): UUID {
        // Generate random bits for the UUID
        val randomMsb = Random.nextLong()
        val randomLsb = Random.nextLong()

        // Set the version bits to 7
        // Clear the version bits (bits 12-15) and then set them to 7
        val msb = (randomMsb and -0x1000) or 0x7000

        // Set the variant bits to 10 (binary) or 2 (decimal)
        // Clear the variant bits (bits 62-63) and then set them to 10 (binary)
        // We use Long.MIN_VALUE which is 0x8000000000000000L (bit 63 set to 1)
        val lsb = (randomLsb and Long.MAX_VALUE) or Long.MIN_VALUE

        return UUID(msb, lsb)
    }
}
