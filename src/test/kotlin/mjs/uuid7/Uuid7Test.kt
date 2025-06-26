package mjs.uuid7

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import java.util.UUID

class Uuid7Test : FunSpec({
    test("it returns a Uuid instance that is non-zero") {
        val uuid = Uuid7.generate()
        uuid shouldNotBe UUID(0, 0)
    }
})