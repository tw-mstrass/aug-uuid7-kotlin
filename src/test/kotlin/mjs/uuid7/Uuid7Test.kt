package mjs.uuid7

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
})
