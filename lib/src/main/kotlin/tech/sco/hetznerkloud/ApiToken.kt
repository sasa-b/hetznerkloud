package tech.sco.hetznerkloud

import kotlinx.io.files.Path
import java.io.File

data class ApiToken(
    val value: String,
) {
    override fun toString(): String = "Bearer $value"

    companion object {
        fun load(path: Path): ApiToken =
            ApiToken(
                File(path.toString()).readText(Charsets.UTF_8),
            )
    }
}
