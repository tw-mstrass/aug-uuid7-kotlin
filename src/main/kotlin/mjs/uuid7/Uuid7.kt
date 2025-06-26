package mjs.uuid7

import java.util.UUID
import kotlin.random.Random

object Uuid7 {
    fun generate(): UUID {
        // Generate random bits for the UUID
        val randomMsb = Random.nextLong()
        val lsb = Random.nextLong()

        // Set the version bits to 7
        // Clear the version bits (bits 12-15) and then set them to 7
        val msb = (randomMsb and -0x1000) or 0x7000

        return UUID(msb, lsb)
    }
}
