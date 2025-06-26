package mjs.uuid7

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import java.time.Instant
import java.util.UUID

class Uuid7Test : FunSpec({
    test("it returns a Uuid instance that is non-zero") {
        val uuid = Uuid7.generate()
        uuid shouldNotBe UUID(0, 0)
    }

    test("it returns a Uuid instance that parses correctly") {
        val uuid = Uuid7.generate()
        val uuidString = uuid.toString()
        val parsedUuid = UUID.fromString(uuidString)
        parsedUuid shouldBe uuid
    }

    test("it has the correct version bits (version 7)") {
        val uuid = Uuid7.generate()
        val version = (uuid.mostSignificantBits shr 12 and 0x0F).toInt()
        version shouldBe 7
    }

    test("it has the correct variant bits (variant 10)") {
        val uuid = Uuid7.generate()
        val variant = (uuid.leastSignificantBits ushr 62 and 0x03).toInt()
        variant shouldBe 2 // Binary 10
    }

    test("it has the correct string representation format") {
        val uuid = Uuid7.generate()
        val uuidString = uuid.toString()

        // UUID format: 8-4-4-4-12 hexadecimal digits
        // Version 7 UUIDs have the 13th character as '7'
        // Variant 10 UUIDs have the 17th character as '8', '9', 'a', or 'b'
        uuidString shouldMatch "[0-9a-f]{8}-[0-9a-f]{4}-7[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}".toRegex(RegexOption.IGNORE_CASE)
    }

    test("the timestamp component reflects the current time") {
        val beforeMs = Instant.now().toEpochMilli()
        val uuid = Uuid7.generate()
        val afterMs = Instant.now().toEpochMilli()

        // Extract the timestamp from the UUID
        val timestamp = Uuid7.extractTimestamp(uuid)

        // The timestamp should be between beforeMs and afterMs
        timestamp shouldBeGreaterThanOrEqual beforeMs
        timestamp shouldBeLessThanOrEqual afterMs
    }

    test("UUIDs created in sequence have monotonically increasing timestamps") {
        val count = 1000
        val uuids = List(count) { Uuid7.generate() }

        // Extract timestamps from all UUIDs
        val timestamps = uuids.map { Uuid7.extractTimestamp(it) }

        // Check that each timestamp is greater than or equal to the previous one
        for (i in 1 until timestamps.size) {
            timestamps[i] shouldBeGreaterThanOrEqual timestamps[i-1]
        }
    }
})
