package mjs.uuid7

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.collections.shouldBeSortedWith
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

    test("the random component is properly generated") {
        // Generate a large number of UUIDs with the same timestamp
        val timestamp = Instant.now().toEpochMilli()
        val count = 1000
        val uuids = HashSet<UUID>()

        // Mock the current time to always return the same timestamp
        // This is a simplified approach; in a real test, we might use a clock mock
        repeat(count) {
            val uuid = Uuid7.generate()

            // Verify the UUID has the correct version and variant
            val version = (uuid.mostSignificantBits shr 12 and 0x0F).toInt()
            version shouldBe 7

            val variant = (uuid.leastSignificantBits ushr 62 and 0x03).toInt()
            variant shouldBe 2

            // Add the UUID to the set and verify it's unique
            uuids.add(uuid) shouldBe true
        }

        // Verify we have the expected number of unique UUIDs
        uuids.size shouldBe count
    }

    test("UUIDs generated in rapid succession are unique") {
        // Generate UUIDs as fast as possible
        val count = 10000
        val uuids = HashSet<UUID>()

        // Generate UUIDs in a tight loop to simulate rapid succession
        repeat(count) {
            val uuid = Uuid7.generate()

            // Add the UUID to the set and verify it's unique
            uuids.add(uuid) shouldBe true
        }

        // Verify we have the expected number of unique UUIDs
        uuids.size shouldBe count
    }

    test("UUIDs with the same timestamp have different random components") {
        // Generate a set of UUIDs with the same timestamp
        val count = 1000
        val uuids = mutableListOf<UUID>()
        val randomComponents = HashSet<Long>()

        // Generate UUIDs and extract their random components
        repeat(count) {
            val uuid = Uuid7.generate()
            uuids.add(uuid)

            // Extract the timestamp
            val timestamp = Uuid7.extractTimestamp(uuid)

            // Extract the random component (12 bits from MSB and all bits from LSB except variant bits)
            val msbRandom = uuid.mostSignificantBits and 0x0000_0000_0000_0FFFL
            val lsbRandom = uuid.leastSignificantBits and 0x3FFF_FFFF_FFFF_FFFFL
            val randomComponent = (msbRandom shl 62) or lsbRandom

            // Add the random component to the set
            randomComponents.add(randomComponent)
        }

        // Group UUIDs by timestamp
        val uuidsByTimestamp = uuids.groupBy { Uuid7.extractTimestamp(it) }

        // For each group of UUIDs with the same timestamp, verify they have different random components
        uuidsByTimestamp.forEach { (timestamp, timestampUuids) ->
            if (timestampUuids.size > 1) {
                val randomComponentsForTimestamp = HashSet<Long>()

                timestampUuids.forEach { uuid ->
                    val msbRandom = uuid.mostSignificantBits and 0x0000_0000_0000_0FFFL
                    val lsbRandom = uuid.leastSignificantBits and 0x3FFF_FFFF_FFFF_FFFFL
                    val randomComponent = (msbRandom shl 62) or lsbRandom

                    // Verify this random component is unique for this timestamp
                    randomComponentsForTimestamp.add(randomComponent) shouldBe true
                }

                // Verify we have the expected number of unique random components for this timestamp
                randomComponentsForTimestamp.size shouldBe timestampUuids.size
            }
        }
    }

    test("2000 UUIDs generated quickly sort in the order they are generated, even if they have the same timestamp") {
        // Generate 2000 UUIDs as quickly as possible
        val count = 2000
        val generatedUuids = mutableListOf<UUID>()

        // Generate UUIDs and keep track of their original order
        repeat(count) {
            val uuid = Uuid7.generate()
            generatedUuids.add(uuid)
        }

        // Create a copy of the UUIDs for sorting
        val sortedUuids = generatedUuids.toList()

        // Sort the UUIDs
        val sortedByUuidOrder = sortedUuids.sorted()

        // Verify that the UUIDs are sorted in the same order they were generated
        // This means that even UUIDs with the same timestamp should maintain their generation order
        for (i in 0 until count) {
            sortedByUuidOrder[i] shouldBe generatedUuids[i]
        }
    }
})
