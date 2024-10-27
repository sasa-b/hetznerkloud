package tech.sco.hetznerkloud

import io.ktor.http.HttpMethod

typealias HttpMethodAndPath = Pair<HttpMethod, String>

enum class Route(
    val value: HttpMethodAndPath,
) {
    GET_ALL_SERVERS(Pair(HttpMethod.Get, "/servers")),
    CREATE_SERVER(Pair(HttpMethod.Post, "/servers")),

    GET_ALL_DATACENTERS(Pair(HttpMethod.Get, "/datacenters")),

    GET_ALL_IMAGES(Pair(HttpMethod.Get, "/images")),

    GET_ALL_ISOS(Pair(HttpMethod.Get, "/isos")),

    GET_ALL_SERVER_TYPES(Pair(HttpMethod.Get, "/server-types")),
}
