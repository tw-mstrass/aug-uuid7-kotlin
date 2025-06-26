package mjs.uuid7

import java.io.File

fun main() {
    val filename = "uuid7_data.csv"
    File(filename).bufferedWriter().use { writer ->
        writer.appendLine("rownum,original,sorted")
        val uuids = List(7_000) { Uuid7.generate() }
        val sorted = uuids.sorted()
        var rownum = 0
        uuids.zip(sorted) { original, sorted ->
            writer.appendLine("${++rownum},$original,$sorted")
        }
    }
}