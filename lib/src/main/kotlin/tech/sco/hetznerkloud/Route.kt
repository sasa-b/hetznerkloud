package tech.sco.hetznerkloud

import io.ktor.http.HttpMethod
import tech.sco.hetznerkloud.model.ResourceId

internal typealias HttpMethodAndPath = Pair<HttpMethod, Path>

internal data class Path(val value: String) {
    fun withId(id: ResourceId) = Path(value.replace("{id}", id.toString()))
}

internal enum class Route(
    val value: HttpMethodAndPath,
) {
    GET_ALL_ACTIONS(Pair(HttpMethod.Get, Path("/actions"))),
    GET_ACTION(Pair(HttpMethod.Get, Path("/actions/{id}"))),

    GET_ALL_SERVERS(Pair(HttpMethod.Get, Path("/servers"))),
    GET_SERVER(Pair(HttpMethod.Get, Path("/servers/{id}"))),
    GET_SERVER_METRICS(Pair(HttpMethod.Get, Path("/servers/{id}/metrics"))),
    CREATE_SERVER(Pair(HttpMethod.Post, Path("/servers"))),
    UPDATE_SERVER(Pair(HttpMethod.Patch, Path("/servers/{id}"))),
    DELETE_SERVER(Pair(HttpMethod.Delete, Path("/servers/{id}"))),

    GET_ALL_DATACENTERS(Pair(HttpMethod.Get, Path("/datacenters"))),
    GET_DATACENTER(Pair(HttpMethod.Get, Path("/datacenters/{id}"))),

    GET_ALL_IMAGES(Pair(HttpMethod.Get, Path("/images"))),
    GET_IMAGE(Pair(HttpMethod.Get, Path("/images/{id}"))),
    UPDATE_IMAGE(Pair(HttpMethod.Patch, Path("/images/{id}"))),
    DELETE_IMAGE(Pair(HttpMethod.Delete, Path("/images/{id}"))),

    GET_ALL_ISOS(Pair(HttpMethod.Get, Path("/isos"))),
    GET_ISO(Pair(HttpMethod.Get, Path("/isos/{id}"))),

    GET_ALL_SERVER_TYPES(Pair(HttpMethod.Get, Path("/server_types"))),
    GET_SERVER_TYPE(Pair(HttpMethod.Get, Path("/server_types/{id}"))),

    GET_ALL_PLACEMENT_GROUPS(Pair(HttpMethod.Get, Path("/placement_groups"))),
    GET_A_PLACEMENT_GROUP(Pair(HttpMethod.Get, Path("/placement_groups/{id}"))),
    CREATE_PLACEMENT_GROUP(Pair(HttpMethod.Post, Path("/placement_groups"))),
    UPDATE_PLACEMENT_GROUP(Pair(HttpMethod.Patch, Path("/placement_groups/{id}"))),
    DELETE_PLACEMENT_GROUP(Pair(HttpMethod.Delete, Path("/placement_groups/{id}"))),
}
