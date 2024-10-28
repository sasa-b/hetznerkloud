package tech.sco.hetznerkloud

import io.ktor.http.HttpMethod

typealias HttpMethodAndPath = Pair<HttpMethod, Path>

data class Path(val value: String) {
    fun withId(id: Int) = Path(value.replace("{id}", id.toString()))
}

enum class Route(
    val value: HttpMethodAndPath,
) {
    GET_ALL_SERVERS(Pair(HttpMethod.Get, Path("/servers"))),
    GET_SERVER(Pair(HttpMethod.Get, Path("/servers/{id}"))),
    CREATE_SERVER(Pair(HttpMethod.Post, Path("/servers"))),

    GET_ALL_DATACENTERS(Pair(HttpMethod.Get, Path("/datacenters"))),
    GET_DATACENTER(Pair(HttpMethod.Get, Path("/datacenters/{id}"))),

    GET_ALL_IMAGES(Pair(HttpMethod.Get, Path("/images"))),
    GET_IMAGE(Pair(HttpMethod.Get, Path("/images/{id}"))),

    GET_ALL_ISOS(Pair(HttpMethod.Get, Path("/isos"))),
    GET_ISO(Pair(HttpMethod.Get, Path("/isos/{id}"))),

    GET_ALL_SERVER_TYPES(Pair(HttpMethod.Get, Path("/server-types"))),
    GET_SERVER_TYPE(Pair(HttpMethod.Get, Path("/server-types/{id}"))),
}
