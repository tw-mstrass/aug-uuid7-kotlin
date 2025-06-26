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
})
