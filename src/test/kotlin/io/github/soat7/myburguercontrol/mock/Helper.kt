package io.github.soat7.myburguercontrol.mock

import java.io.File

fun jsonResource(jsonResource: String, context: Map<String, String>): String {
    var json = File("src/test/resources/mocks/$jsonResource").readText()
    context.forEach { (k, v) -> json = json.replace("{{$k}}", v) }
    return json
}
