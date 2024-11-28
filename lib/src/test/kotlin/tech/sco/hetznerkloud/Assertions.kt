package tech.sco.hetznerkloud

import io.kotest.assertions.json.shouldEqualJson
import java.io.File

infix fun String.shouldBeEqualToRequest(pathToExpected: String) {
    this.shouldEqualJson(File("src/test/resources/examples/request/$pathToExpected").readText(Charsets.UTF_8))
}
