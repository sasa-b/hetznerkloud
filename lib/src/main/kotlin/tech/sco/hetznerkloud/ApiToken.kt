package tech.sco.hetznerkloud

import java.io.File
import java.nio.file.Path

data class ApiToken(val value: String) {
    override fun toString(): String = "Bearer $value"

    companion object {
        fun file(path: Path): ApiToken = ApiToken(
            File(path.toString()).readText(Charsets.UTF_8),
        )

        fun env(name: String): ApiToken? = System.getenv(name)?.let { ApiToken(it) }
    }
}
